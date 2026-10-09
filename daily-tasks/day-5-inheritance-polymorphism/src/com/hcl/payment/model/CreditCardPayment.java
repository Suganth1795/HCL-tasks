package com.hcl.payment.model;

/**
 * Concrete Credit Card Payment implementation.
 * Extends Payment and implements Refundable interface.
 */
public class CreditCardPayment extends Payment implements Refundable {

    private final String cardNumber;
    private final String cardHolderName;
    private final String expiryDate;

    public CreditCardPayment(double amount, String cardNumber, String cardHolderName, String expiryDate) {
        super(amount);
        if (cardNumber == null || cardNumber.length() < 12) {
            throw new IllegalArgumentException("Invalid credit card number format.");
        }
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
    }

    private String getMaskedCardNumber() {
        int len = cardNumber.length();
        return "****-****-****-" + cardNumber.substring(len - 4);
    }

    @Override
    public boolean processPayment() {
        System.out.println("💳 Processing Credit Card payment via Payment Gateway...");
        System.out.printf("   Card Holder: %s | Card: %s | Expiry: %s\n",
                cardHolderName, getMaskedCardNumber(), expiryDate);
        System.out.printf("   Charging: $%,.2f\n", getAmount());

        // Simulate successful gateway authorization
        setStatus("SUCCESS");
        System.out.println("✅ Authorization Approved! Authorization Code: AUTH-" + (int)(Math.random() * 900000 + 100000));
        return true;
    }

    @Override
    public boolean issueRefund(double amount) {
        if (!"SUCCESS".equals(getStatus())) {
            System.out.println("❌ Refund Rejected: Cannot refund an uncompleted transaction.");
            return false;
        }
        if (amount <= 0 || amount > getAmount()) {
            System.out.printf("❌ Refund Error: Invalid refund amount $%.2f (Original: $%.2f)\n", amount, getAmount());
            return false;
        }

        double fee = calculateRefundFee(amount);
        double netRefund = amount - fee;

        System.out.println("🔄 Initiating Credit Card Refund...");
        System.out.printf("   Refunding to Card: %s | Gross: $%,.2f | Processing Fee (2%%): $%,.2f | Net Credited: $%,.2f\n",
                getMaskedCardNumber(), amount, fee, netRefund);

        setStatus("REFUNDED");
        System.out.println("✅ Card Refund processed successfully back to issuing bank.");
        return true;
    }

    @Override
    public double calculateRefundFee(double amount) {
        return amount * 0.02; // 2% gateway processing fee
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [CREDIT_CARD: %s, Holder: %s]", getMaskedCardNumber(), cardHolderName);
    }
}
