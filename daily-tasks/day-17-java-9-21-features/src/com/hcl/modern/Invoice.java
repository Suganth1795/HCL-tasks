package com.hcl.modern;

import java.time.LocalDate;

/**
 * Invoice record generating formatted notification email using Java 15 text blocks.
 */
public record Invoice(
        String invoiceNumber,
        String clientName,
        String serviceDescription,
        int quantity,
        double unitRate,
        LocalDate issueDate,
        LocalDate dueDate
) {
    public double totalAmount() {
        return quantity * unitRate;
    }

    /**
     * Generates a multi-line formatted email message using Modern Java text blocks.
     */
    public String generateEmailTemplate() {
        return """
        ========================================================================
                             OFFICIAL TAX INVOICE
        ========================================================================
        Invoice ID   : %s
        Issue Date   : %s
        Due Date     : %s
        ------------------------------------------------------------------------
        Billed To:
          Client Name: %s
        ------------------------------------------------------------------------
        Line Items:
          Service    : %s
          Quantity   : %d units
          Unit Rate  : $%.2f
        ------------------------------------------------------------------------
        TOTAL AMOUNT DUE: $%.2f
        ------------------------------------------------------------------------
        Payment Instructions:
          Please transfer payment before %s to:
          Bank   : HCL Corporate Treasury
          A/C    : 987654321098
          SWIFT  : HCLTINBBXXX

        For billing discrepancies, contact support-finance@hcltech.com.
        ========================================================================
        """.formatted(
                invoiceNumber,
                issueDate,
                dueDate,
                clientName,
                serviceDescription,
                quantity,
                unitRate,
                totalAmount(),
                dueDate
        );
    }
}
