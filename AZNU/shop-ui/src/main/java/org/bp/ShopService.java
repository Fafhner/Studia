package org.bp;

import static org.apache.camel.model.rest.RestParamType.body;
import static org.bp.shop.Utils.*;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.bp.courier.CourierRequest;
import org.bp.shop.ShopRequest;
import org.bp.storage.StorageInfo;
import org.springframework.stereotype.Component;

@Component
public class ShopService extends RouteBuilder {

    @org.springframework.beans.factory.annotation.Autowired
    ShopIdentifierService shopIdentifierService;

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


        rest("/shop").description("Shop REST service")
                .consumes("application/json")
                .produces("application/json")
                .post("/items").description("Item Shop").type(ShopRequest.class).outType(StorageInfo.class)
                .param().name("body").type(body).description("Shopping and delivery").endParam()
                .responseMessage().code(200).message("Items purchased successfully").endResponseMessage()
                .to("direct:shopItems");

        from("direct:shopItems").routeId("shopItems")
                .log("shopItems fired")
                .process((exchange) -> {
                    Integer id = shopIdentifierService.generateShopId();
                    exchange.getMessage().setHeader(
                            "shopBasketId", Integer.toString(id)
                    );
                    ShopRequest sr = exchange.getMessage().getBody(ShopRequest.class);
                    System.out.println("Body: " + sr.getCourierType());
                })
                .to("direct:ShopItemsRequest")
                .to("direct:shopItemsRequester");


        from("direct:shopItemsRequester").routeId("shopItemsRequester")
                .log("bookRequester fired")
                .process(
                        (exchange) -> {
                            exchange.getMessage().setBody(prepareStorageInfo(Integer.valueOf(
                                    exchange.getMessage().getHeader("shopBasketId", String.class)), -1));
                        }
                )
        ;

        from("direct:ShopItemsRequest").routeId("ShopItemsRequest")
                .process((exchange -> {
                    Integer id = shopIdentifierService.generateShopId();
                    exchange.getMessage().setHeader(
                                    "shopBasketId", Integer.toString(id)
                                    );
                    ShopRequest sr = exchange.getMessage().getBody(ShopRequest.class);
                    shopIdentifierService.assignShopRequest(id, sr);

                    ObjectMapper mapper = new ObjectMapper();

                    String jsonString = mapper.writeValueAsString(sr);
                    exchange.getMessage().setBody(jsonString);
                    System.out.println("Headers: " + exchange.getMessage().getHeaders().toString());
                }))
                .log("ShopItemsRequest fired")
                .to("kafka:ShopReqTopic?brokers=localhost:9092")
        ;

//        from("direct:shopItems").routeId("shopItems").log("shopItems fired")
//                .process(
//                        (exchange) -> {
//                            Integer id = shopIdentifierService.generateShopId();
//                            exchange.getMessage().setHeader(
//                                            "shopBasketId", Integer.toString(id)
//                                            );
//                            ShopRequest sr = exchange.getMessage().getBody(ShopRequest.class);
//                            shopIdentifierService.assignShopRequest(id, sr);
//                        })
//                //.to("direct:shopRequest")
//                .to("kafka:ShopReqTopic?brokers=localhost:9092");
//
//        from("kafka:ShopReqTopic?brokers=localhost:9092")
//                .routeId("test").log("test fired")
//               // .unmarshal().json(JsonLibrary.Jackson, ShopRequest.class)
//                .process(
//                        (exchange) -> {
//                            String srstr = exchange.getMessage().getBody(String.class);
//                            ShopRequest sr = new ObjectMapper().readValue(srstr, ShopRequest.class);
//                            System.out.println(exchange.getMessage().getHeader("shopBasketId", String.class));
//
//                            System.out.println("Body: " + sr.getCourierType());
//                            StorageInfo storageInfo = new StorageInfo();
//                            storageInfo.setCost(0);
//                            storageInfo.setId(99);
//                            exchange.getMessage().setBody(storageInfo);
//                        });






    }

    public void storage() {
        //from("direct:shopRequest")
//                .routeId("shopRequest")
//                .log("shopRequest fired")
//                .process((exchange) -> {
//                    ShopRequest sr = shopIdentifierService.getShopRequest(
//                            exchange.getMessage().getHeader("shopBasketId", Integer.class));
//                    exchange.getMessage().setBody(preparePurchaseItemsRequest(sr));
//                })
//                .to("spring-ws:http://localhost:8080/soap-api/service/storage")
//                .unmarshal(jaxbFlight).to("direct:shopResponse");


//        from("direct:shopResponse")
//                .routeId("shopResponse")
//                .log("shopResponse fired")
//                .process((exchange -> {
//                    System.out.println("Headers: " + exchange.getMessage().getHeader("shopBasketId"));
//                    StorageInfo storageInfo = exchange.getMessage().getBody(StorageInfo.class);
//                    shopIdentifierService.assignStorageTransaction(
//                            exchange.getMessage().getHeader("shopBasketId", Integer.class),
//                            storageInfo);
//                    exchange.getMessage().setBody(storageInfo);
//                    StorageInfo storageInfo = new StorageInfo();
//                    storageInfo.setCost(0);
//                    storageInfo.setId(99);
//                    exchange.getMessage().setBody(storageInfo);
//                }));

    }

    public void courier() {

        from("kafka:ShopReqTopic?brokers=localhost:9092")
                .routeId("shopCourier")
                .log("shopCourier fired").unmarshal().json()
                .process((exchange) -> {
                    ShopRequest sr = shopIdentifierService.getShopRequest(Integer.parseInt(
                            exchange.getMessage().getHeader("shopBasketId", String.class)));
                    System.out.println(exchange.getMessage().getHeader("shopBasketId", Integer.class));
                    exchange.getMessage().setBody(prepareCourierRequest(sr));

                    System.out.println("Headers: " + exchange.getMessage().getHeaders().toString());
                })
                .removeHeaders("CamelHttp*")
                .to("rest:post:/courier?host=localhost:8083")
                .to("stream:out");

        //from("direct:courierResponse")
//                .routeId("courierResponse")
//                .log("courierResponse fired")
//                .process((exchange) -> {
//                    HashMap isin  = exchange.getMessage().getBody(HashMap.class);
//
////                    shopIdentifierService.assignCourierTransaction(
////                            exchange.getMessage().getHeader("shopBasketId", Integer.class),
////                            courierResponse);
//                    System.out.println("Courier response id:" + isin.get("courierId"));
//                    StorageInfo storageInfo = new StorageInfo();
//                    storageInfo.setCost(0);
//                    storageInfo.setId(99);
//                    exchange.getMessage().setBody(storageInfo);
//                });
    }


    @Override
    public void configure() throws Exception {
       gateway();
       courier();
    }
}
