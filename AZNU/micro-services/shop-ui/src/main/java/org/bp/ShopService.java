package org.bp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.bp.kafka.KafkaHeaderDeserializer;
import org.bp.kafka.KafkaHeaderSerializer;
import org.bp.models.courier.CourierResponse;
import org.bp.models.courier.ExceptionResponse;
import org.bp.models.payment.Amount;
import org.bp.models.payment.PaymentRequest;
import org.bp.models.payment.PaymentResponse;
import org.bp.models.shop.Notification;
import org.bp.models.shop.Response;
import org.bp.models.shop.ShopRequest;
import org.bp.models.shop.Utils;
import org.bp.models.storage.*;
import org.bp.state.ProcessingEvent;
import org.bp.state.ProcessingState;
import org.bp.state.StateService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.support.builder.PredicateBuilder.not;
import static org.bp.models.shop.Utils.*;

@Component
public class ShopService extends RouteBuilder {

    @org.springframework.beans.factory.annotation.Autowired
    ShopIdentifierService shopIdentifierService;

    @org.springframework.beans.factory.annotation.Autowired
    CamelContext camelContext;

    @org.springframework.beans.factory.annotation.Autowired
    StateService storageStateService;

    @org.springframework.beans.factory.annotation.Autowired
    StateService courierStateService;
    HashMap<Integer, RouteDefinition> endRoutes = new HashMap<>();
    @org.springframework.beans.factory.annotation.Value("${shop.kafka.server}")
    private String shopKafkaServer;
    @org.springframework.beans.factory.annotation.Value("${shop.storage.service}")
    private String shopStorageService;
    @org.springframework.beans.factory.annotation.Value("${shop.courier.service}")
    private String shopCourierService;
    @org.springframework.beans.factory.annotation.Value("${shop.payment.service}")
    private String shopPaymentService;


    @Override
    public void configure() {
        camelContext.getRegistry().bind("kafkaSerializer", new KafkaHeaderSerializer());
        camelContext.getRegistry().bind("kafkaDeserializer", new KafkaHeaderDeserializer());

        storageExceptionHandlers();
        shippingExceptionHandlers();
        paymentExceptionHandlers();

        gateway();
        storage();
        storageCompensation();
        shipping();
        shippingCompensation();
        payment();

        configureReceive();
        configureGetItems();
    }

    private void gateway() {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .contextPath("/api")
                // turn on swagger api-doc
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Shop API")
                .apiProperty("api.version", "1.0.0");


        rest("/shop")
                .description("Shop REST service")
                .consumes("application/json")
                .produces("application/json")
                .post("/items").description("Item Shop").type(ShopRequest.class).outType(Notification.class)
                .param().name("body").type(body).description("Shopping and delivery").endParam()
                .responseMessage().code(200).message("Items purchased successfully").endResponseMessage()
                .to("direct:shopItems")
        ;

        from("direct:shopItems")
                .routeId("shopItems")
                .log("shopItems fired")
                .process((exchange) -> {
                    Integer id = shopIdentifierService.generateShopId();

                    exchange.getMessage().setHeader(
                            "shopRequestId", Integer.toString(id)
                    );

                })
                .to("direct:ShopItemsRequest");


        from("direct:ShopItemsRequest")
                .routeId("ShopItemsRequest")
                .log("ShopItemsRequest fired")
                .process((exchange -> {
                    String shopRequestId = (String) exchange.getMessage().getHeaders().get("shopRequestId");
                    ShopRequest sr = exchange.getMessage().getBody(ShopRequest.class);

                    shopIdentifierService.assignShopRequest(Integer.valueOf(shopRequestId), sr);

                }))
                .to("kafka:ShopReqTopic?brokers=" + shopKafkaServer +  "&kafkaHeaderSerializer=#kafkaSerializer")
                .log("To " + "kafka:ShopReqTopic?brokers=" + shopKafkaServer)
                .to("direct:receiveId");


        from("direct:receiveId")
                .routeId("receiveId")
                .log("receiveId fired")
                .process(
                        exchange -> {
                            int shopRequestId = Integer.parseInt(
                                    exchange.getMessage().getHeader("shopRequestId", String.class));

                            Response r = new Response();
                            r.setBasketId(shopRequestId);
                            exchange.getMessage().setBody(r);

                        }
                );

    }

    public void storage() {

        final JaxbDataFormat jaxbHotel = new JaxbDataFormat(StorageInfo.class.getPackage().getName());

        from("kafka:ShopReqTopic?brokers=" + shopKafkaServer + "&kafkaHeaderDeserializer=#kafkaDeserializer")
                .routeId("shopStorageRequest")
                .log("Shop Storage Request fired")
                .process((exchange) -> {
                    String shopRequestId = exchange.getMessage().getHeader("shopRequestId", String.class);

                    ShopRequest sr = shopIdentifierService.getShopRequest(Integer.valueOf(shopRequestId));
                    exchange.getMessage().setBody(preparePurchaseItemsRequest(sr));

                    ProcessingState previousState = storageStateService.sendEvent(shopRequestId, ProcessingEvent.START);
                    exchange.getMessage().setHeader("previousState", previousState);

                })
                .setHeader("serviceType", constant("storage"))
                .choice().when(header("previousState").isNotEqualTo(ProcessingState.CANCELLED))
                .marshal(jaxbHotel)
                .to("spring-ws:http://"+shopStorageService+"/soap-api/service/storage")
                .unmarshal(jaxbHotel)
                .to("direct:shopStorageResponse")
                .endChoice();


        from("direct:shopStorageResponse")
                .routeId("shopStorageResponse")
                .log("Shop Storage Response fired")
                .process((exchange -> {
                    String shopRequestId = (String) exchange.getMessage().getHeaders().get("shopRequestId");

                    StorageInfo storageInfo = exchange.getMessage().getBody(StorageInfo.class);
                    shopIdentifierService.assignStorageTransaction(Integer.valueOf(shopRequestId), storageInfo);

                    ProcessingState previousState = storageStateService.sendEvent(shopRequestId, ProcessingEvent.FINISH);
                    exchange.getMessage().setHeader("previousState", previousState);

                }))
                .choice().when(header("previousState").isNotEqualTo(ProcessingState.CANCELLED))
                .to("kafka:ShopResponseTopic?brokers=" + shopKafkaServer
                        + "&kafkaHeaderSerializer=#kafkaSerializer")
                .otherwise()
                .to("direct:storageCompensationAction")
                .endChoice();

    }

    public void shipping() {

        from("kafka:ShopReqTopic?brokers=" + shopKafkaServer + "&kafkaHeaderDeserializer=#kafkaDeserializer")
                .routeId("shopCourier")
                .log("shopCourier fired")
                .process((exchange) -> {

                    String shopRequestId = (String) exchange.getMessage().getHeaders().get("shopRequestId");
                    ShopRequest sr = shopIdentifierService.getShopRequest(Integer.valueOf(shopRequestId));

                    ProcessingState previousState = courierStateService.sendEvent(shopRequestId, ProcessingEvent.START);
                    exchange.getMessage().setHeader("previousState", previousState);

                    exchange.getMessage().setBody(prepareCourierRequest(sr));

                })
                .setHeader("serviceType", constant("courier"))
                .marshal().json()
                .removeHeaders("CamelHttp*")
                .choice().when(header("previousState").isNotEqualTo(ProcessingState.CANCELLED))
                .to("rest:post:/courier?host="+shopCourierService)
                .to("direct:courierResponse")
                .endChoice();

        from("direct:courierResponse")
                .routeId("courierResponse")
                .log("courierResponse fired")
                .process((exchange) -> {
                    String shopRequestId = (String) exchange.getMessage().getHeaders().get("shopRequestId");

                    String courierResponseJSON = exchange.getMessage().getBody(String.class);
                    ObjectMapper mapper = new ObjectMapper();
                    CourierResponse courierResponse = mapper.readValue(courierResponseJSON, CourierResponse.class);
                    shopIdentifierService.assignCourierTransaction(Integer.valueOf(shopRequestId), courierResponse);

                    ProcessingState previousState = courierStateService.sendEvent(shopRequestId, ProcessingEvent.FINISH);
                    exchange.getMessage().setHeader("previousState", previousState);
                })
                .choice().when(header("previousState").isNotEqualTo(ProcessingState.CANCELLED))
                .to("kafka:ShopResponseTopic?brokers=" + shopKafkaServer
                        + "&kafkaHeaderSerializer=#kafkaSerializer")
                .otherwise()
                .to("direct:shippingCompensationAction")
                .endChoice();


    }


    void payment() {
        from("kafka:ShopResponseTopic?brokers=" + shopKafkaServer
                + "&kafkaHeaderDeserializer=#kafkaDeserializer")
                .routeId("shopCheckPayment")
                .log("Shop check payment fired")
                .process((exchange) -> {
                    String shopRequestId = (String) exchange.getMessage().getHeaders().get("shopRequestId");
                    boolean isReady = shopIdentifierService.preparedForPayment(Integer.valueOf(shopRequestId));

                    exchange.getMessage().setHeader("isReady", isReady);

                })
                .choice()
                .when(header("isReady").isEqualTo(true)).to("direct:finalizePayment")
                .endChoice();

        from("direct:finalizePayment")
                .routeId("finalizePayment")
                .log("fired finalizePayment")
                .process((exchange) -> {
                    String shopRequestId = (String) exchange.getMessage().getHeaders().get("shopRequestId");
                    ShopRequest sr = shopIdentifierService.getShopRequest(Integer.valueOf(shopRequestId));

                    PaymentRequest paymentRequest = preparePaymentRequest(sr);

                    CourierResponse courierResponse = shopIdentifierService.getCourierTransaction(Integer.valueOf(shopRequestId));
                    StorageInfo storageInfo = shopIdentifierService.getStorageTransaction(Integer.valueOf(shopRequestId));

                    Amount amount = new Amount();
                    amount.currency("zl");
                    amount.setValue(BigDecimal.valueOf(storageInfo.getCost() + courierResponse.getCost()));
                    paymentRequest.setAmount(amount);

                    exchange.getMessage().setBody(paymentRequest);

                })
                .marshal().json()
                .removeHeaders("CamelHttp*")
                .to("rest:post:/payment?host="+ shopPaymentService)
                .to("stream:out")
                .to("direct:paymentResponse");

        from("direct:paymentResponse")
                .routeId("paymentResponse")
                .log("Payment response fired")
                .process((exchange) -> {
                    String shopRequestId = (String) exchange.getMessage().getHeaders().get("shopRequestId");

                    courierStateService.sendEvent(shopRequestId, ProcessingEvent.COMPLETE);
                    storageStateService.sendEvent(shopRequestId, ProcessingEvent.COMPLETE);

                    String paymentResponseJSON = exchange.getMessage().getBody(String.class);
                    ObjectMapper mapper = new ObjectMapper();

                    PaymentResponse paymentResponse = mapper.readValue(paymentResponseJSON, PaymentResponse.class);
                    shopIdentifierService.assignPaymentTransaction(Integer.valueOf(shopRequestId), paymentResponse);

                    shopIdentifierService.setCompleted(Integer.parseInt(shopRequestId), true);
                });

    }


    private void storageExceptionHandlers() {
        onException(org.springframework.ws.soap.client.SoapFaultClientException.class).process((exchange) -> {
            ExceptionResponse er = new ExceptionResponse();
            er.setTimestamp(OffsetDateTime.now());
            Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
            er.setMessage(cause.getMessage());
            exchange.getMessage().setBody(er);

            int id = Integer.parseInt(exchange.getMessage().getHeader("shopRequestId", String.class));
            List<ExceptionResponse> errors = shopIdentifierService.getErrors(id);

            storageStateService.sendEvent(Integer.toString(id), ProcessingEvent.CANCEL);
            storageStateService.sendEvent(Integer.toString(id), ProcessingEvent.COMPLETE);


            if (errors == null) {
                errors = new ArrayList<>();
                shopIdentifierService.setErrors(id, errors);
            }
            errors.add(er);


        })
                .marshal().json()
                .to("stream:out")
                .setHeader("serviceType", constant("storage"))
                .to("kafka:ShopFailTopic?brokers=" + shopKafkaServer + "&kafkaHeaderSerializer=#kafkaSerializer")
                .handled(true);
    }

    private void shippingExceptionHandlers() {
        onException(org.apache.camel.http.base.HttpOperationFailedException.class).process((exchange) -> {
            ExceptionResponse er = new ExceptionResponse();
            er.setTimestamp(OffsetDateTime.now());
            Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
            er.setMessage(cause.getMessage() + "\n");
            exchange.getMessage().setBody(er);

            int id = Integer.parseInt(exchange.getMessage().getHeader("shopRequestId", String.class));
            List<ExceptionResponse> errors = shopIdentifierService.getErrors(id);

            storageStateService.sendEvent(Integer.toString(id), ProcessingEvent.CANCEL);
            storageStateService.sendEvent(Integer.toString(id), ProcessingEvent.COMPLETE);

            if (errors == null) {
                errors = new ArrayList<>();
                shopIdentifierService.setErrors(id, errors);
            }
            errors.add(er);

        })
                .marshal().json()
                .to("stream:out")
                .setHeader("serviceType", constant("courier"))
                .to("kafka:ShopFailTopic?brokers=" + shopKafkaServer + "&kafkaHeaderSerializer=#kafkaSerializer")
                .handled(true);
    }

    private void paymentExceptionHandlers() {
        onException(org.apache.camel.http.base.HttpOperationFailedException.class).process((exchange) -> {
            ExceptionResponse er = new ExceptionResponse();
            er.setTimestamp(OffsetDateTime.now());
            System.out.println(exchange.getProperties());
            System.out.println(exchange.getMessage().getHeaders());
            Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
            er.setMessage(cause.getMessage() + "\n");
            exchange.getMessage().setBody(er);

            int id = Integer.parseInt(exchange.getMessage().getHeader("shopRequestId", String.class));
            List<ExceptionResponse> errors = shopIdentifierService.getErrors(id);

            if (errors == null) {
                errors = new ArrayList<>();
                shopIdentifierService.setErrors(id, errors);
            }
            errors.add(er);
        })
                .marshal().json()
                .to("stream:out")
                .setHeader("serviceType", constant("payment"))
                .to("kafka:ShopFailTopic?brokers=" + shopKafkaServer +"&kafkaHeaderSerializer=#kafkaSerializer")
                .handled(true);
    }

    private void storageCompensation() {

        Predicate serviceType = header("serviceType").isEqualTo("storage");

        from("kafka:ShopFailTopic?brokers=" + shopKafkaServer + "&kafkaHeaderDeserializer=#kafkaDeserializer")
                .routeId("storageCompensation")
                .log("fired storageCompensation")
                .choice()
                .when(not(serviceType))
                .process((exchange) -> {
                    String shopRequestId = exchange.getMessage().getHeader("shopRequestId", String.class);
                    ProcessingState previousState = storageStateService.sendEvent(shopRequestId, ProcessingEvent.CANCEL);
                    exchange.getMessage().setHeader("previousState", previousState);

                    shopIdentifierService.setCompleted(Integer.parseInt(shopRequestId), isCompleted(shopRequestId)
                    );

                })
                .choice()
                .when(header("previousState").isEqualTo(ProcessingState.FINISHED))
                .to("direct:storageCompensationAction")
                .endChoice()
                .endChoice();

        final JaxbDataFormat jaxbHotel = new JaxbDataFormat(CancelPurchasingResponse.class.getPackage().getName());

        from("direct:storageCompensationAction")
                .routeId("storageCompensationAction")
                .log("fired storageCompensationAction")
                .process(exchange -> {
                    CancelPurchasingRequest request = new CancelPurchasingRequest();
                    String shopRequestId = (String) exchange.getMessage().getHeaders().get("shopRequestId");
                    int id = shopIdentifierService.getStorageTransaction(Integer.parseInt(shopRequestId)).getId();
                    request.setPurchaseId(id);
                    exchange.getMessage().setBody(request);

                    storageStateService.sendEvent(shopRequestId, ProcessingEvent.COMPLETE);
                    shopIdentifierService.setCompleted(Integer.parseInt(shopRequestId), isCompleted(shopRequestId)
                    );

                })
                .marshal(jaxbHotel)
                .to("spring-ws:http://"+shopStorageService+"/soap-api/service/storage")
                .unmarshal(jaxbHotel)
                .to("direct:storageCompensationActionResponse");


        from("direct:storageCompensationActionResponse")
                .routeId("storageCompensationActionResponse")
                .log("fired storageCompensationActionResponse")
                .process(exchange -> {
                    String shopRequestId = exchange.getMessage().getHeader("shopRequestId", String.class);
                })
                .to("stream:out");

    }

    private void shippingCompensation() {
        Predicate serviceType = header("serviceType").isEqualTo("courier");

        from("kafka:ShopFailTopic?brokers=" + shopKafkaServer + "&kafkaHeaderDeserializer=#kafkaDeserializer")
                .routeId("courierCompensation")
                .log("fired courierCompensation")
                .choice()
                .when(not(serviceType))
                .process((exchange) -> {
                    String shopRequestId = exchange.getMessage().getHeader("shopRequestId", String.class);
                    ProcessingState previousState = courierStateService.sendEvent(shopRequestId, ProcessingEvent.CANCEL);
                    exchange.getMessage().setHeader("previousState", previousState);

                    shopIdentifierService.setCompleted(Integer.parseInt(shopRequestId), isCompleted(shopRequestId));

                })
                .choice()
                .when(header("previousState").isEqualTo(ProcessingState.FINISHED))
                .to("direct:shippingCompensationAction")
                .endChoice()
                .endChoice();

        from("direct:shippingCompensationAction")
                .routeId("shippingCompensationAction")
                .log("fired shippingCompensationAction")
                .process(exchange -> {
                    String shopRequestId = (String) exchange.getMessage().getHeaders().get("shopRequestId");
                    int id = shopIdentifierService.getCourierTransaction(Integer.parseInt(shopRequestId)).getCourierId();
                    org.bp.models.courier.CancelRequest request = new org.bp.models.courier.CancelRequest();
                    request.setCourierId(id);
                    exchange.getMessage().setBody(request);

                    courierStateService.sendEvent(shopRequestId, ProcessingEvent.COMPLETE);
                    shopIdentifierService.setCompleted(Integer.parseInt(shopRequestId), isCompleted(shopRequestId)
                    );


                })
                .to("rest:post:/courier/cancel?host="+ shopCourierService)
                .to("direct:shippingCompensationActionResponse");


        from("direct:shippingCompensationActionResponse")
                .routeId("shippingCompensationActionResponse")
                .log("fired shippingCompensationActionResponse")
                .process(exchange -> {
                    String shopRequestId = exchange.getMessage().getHeader("shopRequestId", String.class);
                })
                .to("stream:out");

    }

    public void configureReceive() {

        rest("/shop")
                .description("Shop REST service")
                .consumes("application/json")
                .produces("application/json")
                .get("/result/{id}").description("Get result").outType(Notification.class)
                .param()
                .name("id")
                .description("Basket ID")
                .type(RestParamType.path)
                .dataType("int")
                .endParam()
                .responseMessage().code(200).message("Items purchased successfully").endResponseMessage()
                .to("direct:getNotification");


        from("direct:getNotification")
                .routeId("getNotification")
                .log("Notification fired")
                .process((exchange) -> {
                    int shopRequestId = exchange.getMessage().getHeader("id", Integer.class);

                    while (!shopIdentifierService.isCompleted(shopRequestId)) {
                        //
                    }

                    Notification n = Utils.createNotification(
                            shopRequestId,
                            shopIdentifierService.getShopRequest(shopRequestId),
                            shopIdentifierService.getStorageTransaction(shopRequestId),
                            shopIdentifierService.getCourierTransaction(shopRequestId),
                            shopIdentifierService.getPaymentResponse(shopRequestId),
                            shopIdentifierService.getErrors(shopRequestId)
                    );


                    exchange.getMessage().setBody(n);
                });
    }

    void configureGetItems() {
        rest("/shop")
                .description("Shop REST service")
                .produces("application/json")
                .get("/items").description("Get notification Shop").outType(GetItemsResponse.class)
                .responseMessage().code(200).message("Get items successful").endResponseMessage()
                .to("direct:getItems");

        final JaxbDataFormat jax = new JaxbDataFormat(GetItemsResponse.class.getPackage().getName());

        from("direct:getItems")
                .routeId("getItems")
                .log("getItems fired")
                .process((exchange) -> {
                    GetItemsRequest request = new GetItemsRequest();
                    exchange.getMessage().setBody(request);
                })
                .marshal(jax)
                .to("spring-ws:http://"+shopStorageService+"/soap-api/service/storage")
                .unmarshal(jax)
                .to("direct:getItemsResponse");

        from("direct:getItemsResponse")
                .routeId("getItemsResponse")
                .log("getItemsResponse fired")
                .process((exchange) -> {
                    GetItemsResponse response = exchange.getMessage().getBody(GetItemsResponse.class);

                    ObjectMapper mapper = new ObjectMapper();

                    exchange.getMessage().setBody(mapper.writeValueAsString(response));
                }).to("stream:out");
    }

    public boolean isCompleted(String id) {

        return (courierStateService.getState(id) == ProcessingState.COMPLETED ||
                courierStateService.getState(id) == ProcessingState.CANCELLED) &&
                (storageStateService.getState(id) == ProcessingState.COMPLETED ||
                        storageStateService.getState(id) == ProcessingState.CANCELLED);
    }


}
