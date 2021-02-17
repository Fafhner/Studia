package org.bp.models.shop;
import org.bp.models.courier.Person;
import org.bp.models.payment.PaymentBlik;
import org.bp.models.payment.PaymentCard;
import org.bp.models.storage.StorageItem;

import java.util.List;

public class ShopRequest {
    private Person person;
    private PaymentCard paymentCard;
    private PaymentBlik paymentBlik;
    private List<StorageItem> items;
    private String courierType;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public PaymentCard getPaymentCard() {
        return paymentCard;
    }

    public void setPaymentCard(PaymentCard paymentCard) {
        this.paymentCard = paymentCard;
    }

    public PaymentBlik getPaymentBlik() {
        return paymentBlik;
    }

    public void setPaymentBlik(PaymentBlik paymentBlik) {
        this.paymentBlik = paymentBlik;
    }

    public List<StorageItem> getItems() {
        return items;
    }

    public void setItems(List<StorageItem> items) {
        this.items = items;
    }

    public String getCourierType() {
        return courierType;
    }

    public void setCourierType(String courierType) {
        this.courierType = courierType;
    }
}
