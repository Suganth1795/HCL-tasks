package com.hcl.payment.model;

/**
 * Concrete Unified Payments Interface (UPI) payment implementation.
 * Extends Payment and implements Refundable interface.
 */
public class UPIPayment extends Payment implements Refundable {

    private final String upiId;
    private final String appProvider; // e.g., "Google Pay", "PhonePe", "BHIM"

    public UPIPayment(double amount, String upiId, String appProvider) {
        super(amount);
        if (upiId == null || !upiId.contains("@")) {
            throw new IllegalArgumentException("Invalid UPI ID format (must contain '@').");
        }
        this.upiId = upiId;
        this.appProvider = (appProvider == null || appProvider.isEmpty()) ? "BHIM UPI" : appProvider;
    }

    @Override
    public boolean processPayment() {
        System.out.printf("📱 Processing UPI Payment via [%s]...\n", appProvider);
        System.out.printf("   VPA / Virtual Address: %s | Amount: $%,.2f\n", upiId, getAmount());

        // Simulate instant bank-to-bank settlement
        setStatus("SUCCESS");
        System.out.println("✅ NPCI Instant Settlement Confirmed! RRN: UPI" + (long)(Math.random() * 900000000000L + 100000000000L));
        return true;
    }

    @Override
    public boolean issueRefund(double amount) {
        if (!"SUCCESS".equals(getStatus())) {
            System.out.println("❌ UPI Refund Rejected: Original transaction not successful.");
            return false;
        }
        if (amount <= 0 || amount > getAmount()) {
            System.out.printf("❌ UPI Refund Error: Amount $%.2f exceeds original $%.2f\n", amount, getAmount());
            return false;
        }

        double fee = calculateRefundFee(amount); // Zero fee for direct UPI
        double netRefund = amount - fee;

        System.out.println("🔄 Initiating Instant UPI Reversal / Refund...");
        System.out.printf("   Crediting VPA: %s | Gross: $%,.2f | Fee: $%.2f | Net Credited: $%,.2f\n",
                upiId, amount, fee, netRefund);

        setStatus("REFUNDED");
        System.out.println("✅ Instant UPI credit completed to customer account.");
        return true;
    }

    @Override
    public double calculateRefundFee(double amount) {
        return 0.0; // UPI refunds have 0 processing fees
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [UPI: %s, App: %s]", upiId, appProvider);
    }
}
