package com.hcl.datetime;

import com.hcl.datetime.exception.SubscriptionNotFoundException;
import com.hcl.datetime.model.Subscription;
import com.hcl.datetime.service.SubscriptionService;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Daily Task 15: Optional & Date/Time
 * Topic: Subscription Calculator
 *
 * Requirements:
 * 1. Compute expiry dates, days remaining, grace periods using modern java.time.
 * 2. Perform time zone conversions across UTC, IST (Asia/Kolkata), and EST (America/New_York).
 * 3. Refactor null-heavy lookup service to return Optional and use orElseThrow with custom exception.
 * 4. Strict Constraints: No method returns null; strictly zero usage of legacy java.util.Date or Calendar.
 */
public class SubscriptionCalculator {

    public static final ZoneId ZONE_UTC = ZoneId.of("UTC");
    public static final ZoneId ZONE_IST = ZoneId.of("Asia/Kolkata");
    public static final ZoneId ZONE_EST = ZoneId.of("America/New_York");

    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    public static void displayTimeZoneConversions(ZonedDateTime utcTimestamp) {
        ZonedDateTime istTime = utcTimestamp.withZoneSameInstant(ZONE_IST);
        ZonedDateTime estTime = utcTimestamp.withZoneSameInstant(ZONE_EST);

        System.out.println("  🌐 Original UTC Timestamp : " + utcTimestamp.format(TIME_FORMAT));
        System.out.println("  🇮🇳 Converted to IST       : " + istTime.format(TIME_FORMAT));
        System.out.println("  🇺🇸 Converted to US EST    : " + estTime.format(TIME_FORMAT));
    }

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("   DAILY TASK 15: OPTIONAL & MODERN DATE/TIME (Subscription Calculator) ");
        System.out.println("========================================================================\n");

        LocalDate today = LocalDate.of(2026, 10, 19);
        System.out.println("System Evaluation Date: " + today + "\n");

        SubscriptionService service = new SubscriptionService();

        // 1. Create subscriptions with UTC timestamp and java.time
        ZonedDateTime purchaseTime1 = ZonedDateTime.of(2026, 9, 20, 14, 30, 0, 0, ZONE_UTC);
        Subscription subMonthly = new Subscription("SUB-101", "alice@example.com",
                Subscription.PlanType.MONTHLY, purchaseTime1, LocalDate.of(2026, 9, 20), 5);

        ZonedDateTime purchaseTime2 = ZonedDateTime.of(2026, 8, 1, 9, 15, 0, 0, ZONE_UTC);
        Subscription subQuarterly = new Subscription("SUB-102", "bob@example.com",
                Subscription.PlanType.QUARTERLY, purchaseTime2, LocalDate.of(2026, 8, 1), 7);

        ZonedDateTime purchaseTime3 = ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZONE_UTC);
        Subscription subAnnual = new Subscription("SUB-103", "charlie@example.com",
                Subscription.PlanType.ANNUAL, purchaseTime3, LocalDate.of(2026, 1, 1), 10);

        service.addSubscription(subMonthly);
        service.addSubscription(subQuarterly);
        service.addSubscription(subAnnual);

        // ---------------------------------------------------------------------
        // PART 1: SUBSCRIPTION METRICS & DATE CALCULATIONS
        // ---------------------------------------------------------------------
        System.out.println("--- 1. SUBSCRIPTION VALIDITY, DAYS REMAINING & GRACE PERIOD ---");
        List<Subscription> all = service.findActiveSubscriptions(today);
        System.out.printf("Active / Grace Subscriptions as of %s (%d found):\n", today, all.size());

        for (Subscription sub : all) {
            long daysLeft = sub.getDaysRemaining(today);
            Period remainingPeriod = sub.getRemainingPeriod(today);

            System.out.println("\n  • " + sub);
            System.out.printf("    - Exact Expiry Date          : %s\n", sub.getExpiryDate());
            System.out.printf("    - Days Remaining Until Expiry: %d days\n", daysLeft);
            System.out.printf("    - Remaining Period (Y-M-D)   : %d year(s), %d month(s), %d day(s)\n",
                    remainingPeriod.getYears(), remainingPeriod.getMonths(), remainingPeriod.getDays());
            System.out.printf("    - Is Active Today?           : %b\n", sub.isActive(today));
            System.out.printf("    - Is in Grace Period?        : %b (Grace ends %s)\n",
                    sub.isInGracePeriod(today), sub.getGracePeriodExpiryDate());
        }

        // ---------------------------------------------------------------------
        // PART 2: TIMEZONE CONVERSIONS (UTC -> IST -> EST)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. TIMEZONE CONVERSION ENGINE (UTC <-> IST <-> EST) ---");
        System.out.println("Converting Subscription Purchase Timestamp for SUB-101:");
        displayTimeZoneConversions(subMonthly.getCreatedAtUtc());

        // ---------------------------------------------------------------------
        // PART 3: OPTIONAL REFACTORING & orElseThrow DEMONSTRATION
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. REFACTORED LOOKUP: Optional & orElseThrow ---");

        // Case A: Successful lookup using Optional.ifPresentOrElse
        String existingId = "SUB-102";
        System.out.printf("Searching for existing ID '%s' via Optional:\n", existingId);
        Optional<Subscription> foundOpt = service.findById(existingId);
        foundOpt.ifPresentOrElse(
                sub -> System.out.println("  ✅ Found via Optional.map(): " + sub.getUserEmail() + " on " + sub.getPlanType()),
                () -> System.out.println("  ❌ Not found.")
        );

        // Case B: Transform with Optional.map()
        String maskedEmail = service.findByUserEmail("alice@example.com")
                .map(sub -> sub.getUserEmail().replaceAll("(?<=.{2}).(?=[^@]*?@)", "*"))
                .orElse("N/A");
        System.out.println("  ✅ Masked Email via Optional.map(): " + maskedEmail);

        // Case C: orElseThrow with Custom SubscriptionNotFoundException
        String missingId = "SUB-999";
        System.out.printf("\nAttempting to query non-existent ID '%s' with orElseThrow():\n", missingId);
        try {
            Subscription missing = service.getOrThrow(missingId);
            System.out.println("Retrieved: " + missing);
        } catch (SubscriptionNotFoundException ex) {
            System.out.println("  ✅ Caught Expected Custom Exception: " + ex.getClass().getSimpleName());
            System.out.println("     Error Message: \"" + ex.getMessage() + "\"");
        }

        System.out.println("\n========================================================================\n");
    }
}
