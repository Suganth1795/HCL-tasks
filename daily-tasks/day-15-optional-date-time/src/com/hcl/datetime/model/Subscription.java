package com.hcl.datetime.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Domain entity representing a customer Subscription.
 * Strictly adheres to modern java.time APIs (No java.util.Date).
 */
public class Subscription {

    public enum PlanType {
        MONTHLY(30),
        QUARTERLY(90),
        ANNUAL(365);

        private final int validityDays;

        PlanType(int validityDays) {
            this.validityDays = validityDays;
        }

        public int getValidityDays() {
            return validityDays;
        }
    }

    private final String subscriptionId;
    private final String userEmail;
    private final PlanType planType;
    private final ZonedDateTime createdAtUtc; // UTC Timestamp of purchase
    private final LocalDate startDate;
    private final LocalDate expiryDate;
    private final int gracePeriodDays;

    public Subscription(String subscriptionId, String userEmail, PlanType planType,
                        ZonedDateTime createdAtUtc, LocalDate startDate, int gracePeriodDays) {
        if (subscriptionId == null || subscriptionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Subscription ID cannot be null or empty.");
        }
        if (userEmail == null || !userEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid user email.");
        }

        this.subscriptionId = subscriptionId;
        this.userEmail = userEmail.trim().toLowerCase();
        this.planType = planType;
        this.createdAtUtc = Objects.requireNonNull(createdAtUtc, "Creation timestamp required.");
        this.startDate = Objects.requireNonNull(startDate, "Start date required.");
        this.gracePeriodDays = gracePeriodDays;
        this.expiryDate = startDate.plusDays(planType.getValidityDays());
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public ZonedDateTime getCreatedAtUtc() {
        return createdAtUtc;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public int getGracePeriodDays() {
        return gracePeriodDays;
    }

    public LocalDate getGracePeriodExpiryDate() {
        return expiryDate.plusDays(gracePeriodDays);
    }

    public long getDaysRemaining(LocalDate currentDate) {
        return ChronoUnit.DAYS.between(currentDate, expiryDate);
    }

    public boolean isActive(LocalDate currentDate) {
        return !currentDate.isBefore(startDate) && !currentDate.isAfter(expiryDate);
    }

    public boolean isInGracePeriod(LocalDate currentDate) {
        return currentDate.isAfter(expiryDate) && !currentDate.isAfter(getGracePeriodExpiryDate());
    }

    public Period getRemainingPeriod(LocalDate currentDate) {
        if (currentDate.isAfter(expiryDate)) {
            return Period.ZERO;
        }
        return Period.between(currentDate, expiryDate);
    }

    @Override
    public String toString() {
        return String.format("[%s] User: %-20s | Plan: %-9s | Start: %s | Expiry: %s | Grace: %s",
                subscriptionId, userEmail, planType, startDate, expiryDate, getGracePeriodExpiryDate());
    }
}
