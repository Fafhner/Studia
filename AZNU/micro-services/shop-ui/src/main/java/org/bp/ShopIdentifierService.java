package org.bp;

import org.bp.models.courier.CourierResponse;
import org.bp.models.courier.ExceptionResponse;
import org.bp.models.payment.PaymentResponse;
import org.bp.models.shop.ShopRequest;
import org.bp.models.storage.StorageInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ShopIdentifierService {
    HashMap<Integer, ShopIds> shopIdsMap = new HashMap<>();
    private Integer counter = 0;

    public synchronized Integer generateShopId() {
        Integer shopID = counter;
        ShopIds shopIds = new ShopIds();
        shopIdsMap.put(shopID, shopIds);
        counter += 1;
        return shopID;
    }

    public synchronized void assignShopRequest(Integer shopID, ShopRequest shopRequest) {
        shopIdsMap.get(shopID).setShopRequest(shopRequest);
    }

    public synchronized  void assignStorageTransaction(Integer shopID, StorageInfo storageTransaction) {
        shopIdsMap.get(shopID).setStorageTransaction(storageTransaction);
    }

    public synchronized  void assignCourierTransaction(Integer shopID, CourierResponse courierTransaction) {
        shopIdsMap.get(shopID).setCourierTransaction(courierTransaction);
    }

    public synchronized  void assignPaymentTransaction(Integer shopID, PaymentResponse paymentTransaction) {
        shopIdsMap.get(shopID).setPaymentTransaction(paymentTransaction);
    }

    public ShopRequest getShopRequest(Integer shopID) {
        return shopIdsMap.get(shopID).getShopRequest();
    }
    public synchronized  StorageInfo getStorageTransaction(Integer shopID) {
        return shopIdsMap.get(shopID).getStorageTransaction();
    }

    public synchronized  CourierResponse getCourierTransaction(Integer shopID) {
        return shopIdsMap.get(shopID).getCourierTransaction();
    }

    public synchronized  PaymentResponse getPaymentResponse(Integer shopID) {
        return shopIdsMap.get(shopID).getPaymentTransaction();
    }

    public synchronized boolean preparedForPayment(Integer shopID) {
        ShopIds shopIds = shopIdsMap.get(shopID);

        return shopIds.getStorageTransaction() != null && shopIds.getCourierTransaction() != null;
    }

    public synchronized List<ExceptionResponse> getErrors(Integer id) {
        return shopIdsMap.get(id).getErrors();
    }

    public synchronized void setErrors(Integer id, List<ExceptionResponse> errors) {
        shopIdsMap.get(id).setErrors(errors);
    }

    public synchronized boolean  isCompleted(Integer id) {
        return shopIdsMap.get(id).isCompleted();
    }

    public synchronized  void setCompleted(Integer id, boolean completed) {
        shopIdsMap.get(id).setCompleted(completed);
    }


    public static class ShopIds {
        private ShopRequest shopRequest;
        private StorageInfo storageTransaction;
        private CourierResponse courierTransaction;
        private PaymentResponse paymentTransaction;
        private List<ExceptionResponse> errors;
        private boolean completed = false;

        public ShopRequest getShopRequest() {
            return shopRequest;
        }

        public void setShopRequest(ShopRequest shopRequest) {
            this.shopRequest = shopRequest;
        }

        public StorageInfo getStorageTransaction() {
            return storageTransaction;
        }

        public void setStorageTransaction(StorageInfo storageTransaction) {
            this.storageTransaction = storageTransaction;
        }

        public CourierResponse getCourierTransaction() {
            return courierTransaction;
        }

        public void setCourierTransaction(CourierResponse courierTransaction) {
            this.courierTransaction = courierTransaction;
        }

        public PaymentResponse getPaymentTransaction() {
            return paymentTransaction;
        }

        public void setPaymentTransaction(PaymentResponse paymentTransaction) {
            this.paymentTransaction = paymentTransaction;
        }

        public List<ExceptionResponse> getErrors() {
            return errors;
        }

        public void setErrors(List<ExceptionResponse> errors) {
            this.errors = errors;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
}
