package org.bp.shop;

import org.bp.courier.CourierRequest;
import org.bp.courier.CourierResponse;
import org.bp.payment.Amount;
import org.bp.payment.PaymentRequest;
import org.bp.payment.PaymentResponse;
import org.bp.storage.CancelPurchasingRequest;
import org.bp.storage.PurchaseItemsRequest;
import org.bp.storage.StorageInfo;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;

public class Utils {

    public static PurchaseItemsRequest preparePurchaseItemsRequest(ShopRequest shopRequest) {
        PurchaseItemsRequest purchaseItemsRequest = new PurchaseItemsRequest();
        purchaseItemsRequest.getItems().addAll(shopRequest.getItems());

        org.bp.storage.Person person = new org.bp.storage.Person();
        if (shopRequest.getPerson() != null) {
            person.setFirstName(shopRequest.getPerson().getName());
            person.setLastName(shopRequest.getPerson().getSurname());
        }
        purchaseItemsRequest.setPerson(person);

        return purchaseItemsRequest;
    }

    public static CancelPurchasingRequest prepareCancelPurchasingRequest(Integer transactionId) {
        CancelPurchasingRequest cancelBookingRequest = new CancelPurchasingRequest();
        cancelBookingRequest.setPurchaseId(transactionId);
        return cancelBookingRequest;
    }

    public static StorageInfo prepareStorageInfo(Integer id, Integer cost){
        StorageInfo storageInfo = new StorageInfo();
        storageInfo.setId(id);
        storageInfo.setCost(cost);

        return storageInfo;
    }

    public static CourierRequest prepareCourierRequest(ShopRequest shopRequest) {
        CourierRequest courierRequest = new CourierRequest();
        courierRequest.setPerson(shopRequest.getPerson());
        courierRequest.setCourierType(shopRequest.getCourierType());

        return courierRequest;
    }

    public static CourierResponse createCourierResponse(Integer id) {
        if(id==null) { id = -1; }

        CourierResponse courierResponse = new CourierResponse();
        courierResponse.setCourierId(id);
        courierResponse.setReceiveDate("00");
        courierResponse.setExpectedDeliveryTime("00");
        return courierResponse;
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

    public static PaymentResponse createPaymentResponse() {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTransactionId(-1);
        paymentResponse.setTransactionDate(OffsetDateTime.now());
        return paymentResponse;
    }
}
