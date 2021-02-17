package org.bp.models;

import org.bp.models.courier.Person;
import org.bp.models.payment.PaymentBlik;
import org.bp.models.payment.PaymentCard;

import java.util.List;

public class ShopRequestWrapper {
    private Person person;
    private String paymentType;
    private PaymentCard paymentCard;
    private PaymentBlik paymentBlik;
    private List<StorageItemWrapper> items;
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

    public List<StorageItemWrapper> getItems() {
        return items;
    }

    public void setItems(List<StorageItemWrapper> items) {
        this.items = items;
    }

    public String getCourierType() {
        return courierType;
    }

    public void setCourierType(String courierType) {
        this.courierType = courierType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public String toString() {
        return "ShopRequestWrapper{" +
                "person=" + person +
                ", paymentCard=" + paymentCard +
                ", paymentBlik=" + paymentBlik +
                ", items=" + items +
                ", courierType='" + courierType + '\'' +
                '}';
    }


}
