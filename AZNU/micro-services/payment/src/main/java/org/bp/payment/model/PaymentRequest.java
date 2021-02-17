package org.bp.payment.model;

public class PaymentRequest {
    private PaymentBlik paymentBlik;
    private PaymentCard paymentCard;
    private Amount amount;

    public PaymentBlik getPaymentBlik() {
        return paymentBlik;
    }

    public void setPaymentBlik(PaymentBlik paymentBlik) {
        this.paymentBlik = paymentBlik;
    }

    public PaymentCard getPaymentCard() {
        return paymentCard;
    }

    public void setPaymentCard(PaymentCard paymentCard) {
        this.paymentCard = paymentCard;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}
