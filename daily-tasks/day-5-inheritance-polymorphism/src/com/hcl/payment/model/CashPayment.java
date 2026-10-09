package com.hcl.payment.model;

/**
 * Concrete Cash Payment implementation.
 * Extends Payment. Notice CashPayment does NOT implement digital Refundable.
 */
public class CashPayment extends Payment {

    private final String storeCounterId;
    private final double cashTendered;

    public CashPayment(double amount, String storeCounterId, double cashTendered) {
        super(amount);
        if (cashTendered < amount) {
            throw new IllegalArgumentException("Cash tendered ($" + cashTendered + ") cannot be less than payable amount ($" + amount + ").");
        }
        this.storeCounterId = storeCounterId;
        this.cashTendered = cashTendered;
    }

    public double getChangeDue() {
        return cashTendered - getAmount();
    }

    @Override
    public boolean processPayment() {
        System.out.println("💵 Processing Over-the-Counter Cash Payment...");
        System.out.printf("   Counter ID: %s | Bill Amount: $%,.2f | Cash Received: $%,.2f\n",
                storeCounterId, getAmount(), cashTendered);
        System.out.printf("   Change Returned to Customer: $%,.2f\n", getChangeDue());

        setStatus("SUCCESS");
        System.out.println("✅ Cash Register Drawer Updated. Printed Receipt Generated.");
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [CASH: Counter %s, Change: $%,.2f]", storeCounterId, getChangeDue());
    }
}
