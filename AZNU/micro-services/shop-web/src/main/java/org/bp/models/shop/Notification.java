package org.bp.models.shop;

import com.google.gson.annotations.SerializedName;
import org.bp.models.courier.ExceptionResponse;
import org.bp.models.storage.StorageItem;

import java.util.ArrayList;
import java.util.List;

public class Notification {

    @SerializedName("purchaseId")
    String purchaseId;

    @SerializedName("storageId")
    String storageId;
    @SerializedName("storageCost")
    String storageCost;

    @SerializedName("shippingTransactionId")
    String shippingTransactionId;
    @SerializedName("shippingTransactionDeliveryTime")
    String shippingTransactionDeliveryTime;
    @SerializedName("shippingTransactionCost")
    String shippingTransactionCost;

    @SerializedName("paymentTransactionId")
    String paymentTransactionId;
    @SerializedName("paymentTransactionDate")
    String paymentTransactionDate;
    @SerializedName("paymentTransactionSummaryCost")
    String paymentTransactionSummaryCost;

    @SerializedName("items")
    List<StorageItem> items;

    @SerializedName("errors")
    List<ExceptionResponse> errors;

    public Notification() {
        items = new ArrayList<>();
    }


    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public String getStorageCost() {
        return storageCost;
    }

    public void setStorageCost(String storageCost) {
        this.storageCost = storageCost;
    }

    public String getShippingTransactionId() {
        return shippingTransactionId;
    }

    public void setShippingTransactionId(String shippingTransactionId) {
        this.shippingTransactionId = shippingTransactionId;
    }

    public String getShippingTransactionDeliveryTime() {
        return shippingTransactionDeliveryTime;
    }

    public void setShippingTransactionDeliveryTime(String shippingTransactionDeliveryTime) {
        this.shippingTransactionDeliveryTime = shippingTransactionDeliveryTime;
    }

    public String getShippingTransactionCost() {
        return shippingTransactionCost;
    }

    public void setShippingTransactionCost(String shippingTransactionCost) {
        this.shippingTransactionCost = shippingTransactionCost;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getPaymentTransactionDate() {
        return paymentTransactionDate;
    }

    public void setPaymentTransactionDate(String paymentTransactionDate) {
        this.paymentTransactionDate = paymentTransactionDate;
    }

    public String getPaymentTransactionSummaryCost() {
        return paymentTransactionSummaryCost;
    }

    public void setPaymentTransactionSummaryCost(String paymentTransactionSummaryCost) {
        this.paymentTransactionSummaryCost = paymentTransactionSummaryCost;
    }

    public List<StorageItem> getItems() {
        return items;
    }

    public void setItems(List<StorageItem> items) {
        this.items = items;
    }


    public List<ExceptionResponse> getErrors() {
        return errors;
    }

    public void setErrors(List<ExceptionResponse> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "purchaseId='" + purchaseId + '\'' +
                ", storageId='" + storageId + '\'' +
                ", storageCost='" + storageCost + '\'' +
                ", shippingTransactionId='" + shippingTransactionId + '\'' +
                ", shippingTransactionDeliveryTime='" + shippingTransactionDeliveryTime + '\'' +
                ", shippingTransactionCost='" + shippingTransactionCost + '\'' +
                ", paymentTransactionId='" + paymentTransactionId + '\'' +
                ", paymentTransactionDate='" + paymentTransactionDate + '\'' +
                ", paymentTransactionSummaryCost='" + paymentTransactionSummaryCost + '\'' +
                ", items=" + items +
                ", errors=" + errors +
                '}';
    }
}
