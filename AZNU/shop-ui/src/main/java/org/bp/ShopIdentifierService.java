package org.bp;

import org.bp.courier.CourierResponse;
import org.bp.payment.PaymentResponse;
import org.bp.shop.ShopRequest;
import org.bp.storage.StorageInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ShopIdentifierService {
    HashMap<Integer, ShopIds> shopIdsMap = new HashMap<>();
    private Integer counter = 0;

    public Integer generateShopId() {
        Integer shopID = counter;
        ShopIds shopIds = new ShopIds();
        shopIdsMap.put(shopID, shopIds);
        counter += 1;
        return shopID;
    }

    public void assignShopRequest(Integer shopID, ShopRequest shopRequest) {
        shopIdsMap.get(shopID).setShopRequest(shopRequest);
    }

    public void assignStorageTransaction(Integer shopID, StorageInfo storageTransaction) {
        shopIdsMap.get(shopID).setStorageTransaction(storageTransaction);
    }

    public void assignCourierTransaction(Integer shopID, CourierResponse courierTransaction) {
        shopIdsMap.get(shopID).setCourierTransaction(courierTransaction);
    }

    public void assignPaymentTransaction(Integer shopID, PaymentResponse paymentTransaction) {
        shopIdsMap.get(shopID).setPaymentTransaction(paymentTransaction);
    }

    public ShopRequest getShopRequest(Integer shopID) {
        return shopIdsMap.get(shopID).getShopRequest();
    }
    public StorageInfo getStorageTransaction(Integer shopID) {
        return shopIdsMap.get(shopID).getStorageTransaction();
    }

    public CourierResponse getCourierTransaction(Integer shopID) {
        return shopIdsMap.get(shopID).getCourierTransaction();
    }

    public PaymentResponse getPaymentResponse(Integer shopID) {
        return shopIdsMap.get(shopID).getPaymentTransaction();
    }

    public static class ShopIds {
        private ShopRequest shopRequest;
        private StorageInfo storageTransaction;
        private CourierResponse courierTransaction;
        private PaymentResponse paymentTransaction;

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
    }
}
