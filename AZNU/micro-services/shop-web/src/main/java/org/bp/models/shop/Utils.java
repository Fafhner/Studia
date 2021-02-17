package org.bp.models.shop;

import org.bp.models.courier.CourierRequest;
import org.bp.models.courier.CourierResponse;
import org.bp.models.courier.ExceptionResponse;
import org.bp.models.payment.Amount;
import org.bp.models.payment.PaymentRequest;
import org.bp.models.payment.PaymentResponse;
import org.bp.models.storage.PurchaseItemsRequest;
import org.bp.models.storage.StorageInfo;

import java.math.BigDecimal;
import java.util.List;

public class Utils {

    public static PurchaseItemsRequest preparePurchaseItemsRequest(ShopRequest shopRequest) {
        PurchaseItemsRequest purchaseItemsRequest = new PurchaseItemsRequest();
        purchaseItemsRequest.getItems().addAll(shopRequest.getItems());

        org.bp.models.storage.Person person = new org.bp.models.storage.Person();
        if (shopRequest.getPerson() != null) {
            person.setFirstName(shopRequest.getPerson().getName());
            person.setLastName(shopRequest.getPerson().getSurname());
        }
        purchaseItemsRequest.setPerson(person);

        return purchaseItemsRequest;
    }


    public static CourierRequest prepareCourierRequest(ShopRequest shopRequest) {
        CourierRequest courierRequest = new CourierRequest();
        courierRequest.setPerson(shopRequest.getPerson());
        courierRequest.setCourierType(shopRequest.getCourierType());

        return courierRequest;
    }



    public static PaymentRequest preparePaymentRequest(ShopRequest shopRequest) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentCard(shopRequest.getPaymentCard());
        paymentRequest.setPaymentBlik(shopRequest.getPaymentBlik());
        Amount amount = new Amount();

        amount.setValue(new BigDecimal(0));
        amount.setCurrency("PLN");
        paymentRequest.setAmount(amount);
        return paymentRequest;
    }

    public static Notification createNotification(Integer purchaseId,
                                                  ShopRequest shopRequest,
                                                  StorageInfo storageInfo,
                                                  CourierResponse courierResponse,
                                                  PaymentResponse paymentResponse,
                                                  List<ExceptionResponse> errors) {

        Notification n = new Notification();
        n.setPurchaseId(String.valueOf(purchaseId));

        if (errors == null || errors.isEmpty())
        {
            n.setStorageId(String.valueOf(storageInfo.getId()));
            n.setStorageCost(String.valueOf(storageInfo.getCost()));

            n.setShippingTransactionId(String.valueOf(courierResponse.getCourierId()));
            n.setShippingTransactionCost(String.valueOf(courierResponse.getCost()));
            n.setShippingTransactionDeliveryTime(String.valueOf(courierResponse.getExpectedDeliveryTime()));

            n.setPaymentTransactionId(String.valueOf(paymentResponse.getTransactionId()));
            n.setPaymentTransactionDate(String.valueOf(paymentResponse.getTransactionDate()));
            n.setPaymentTransactionSummaryCost(String.valueOf(courierResponse.getCost() + storageInfo.getCost()));

            n.setItems(shopRequest.getItems());
        }
        else {
            n.setErrors(errors);
        }

        return n;
    }
}
