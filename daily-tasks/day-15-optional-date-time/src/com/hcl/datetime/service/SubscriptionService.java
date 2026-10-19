package com.hcl.datetime.service;

import com.hcl.datetime.exception.SubscriptionNotFoundException;
import com.hcl.datetime.model.Subscription;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service refactored to eliminate null-returns in favor of java.util.Optional.
 * STRICT CONTRACT: No method returns null. Missing values are represented as Optional.empty().
 */
public class SubscriptionService {

    private final Map<String, Subscription> subscriptionRepository;

    public SubscriptionService() {
        this.subscriptionRepository = new HashMap<>();
    }

    public void addSubscription(Subscription subscription) {
        if (subscription == null) {
            throw new IllegalArgumentException("Subscription cannot be null.");
        }
        subscriptionRepository.put(subscription.getSubscriptionId(), subscription);
    }

    /**
     * Refactored lookup returning Optional<Subscription> instead of null.
     */
    public Optional<Subscription> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(subscriptionRepository.get(id));
    }

    /**
     * Finds subscription or throws custom SubscriptionNotFoundException using orElseThrow().
     */
    public Subscription getOrThrow(String id) throws SubscriptionNotFoundException {
        return findById(id).orElseThrow(() ->
                new SubscriptionNotFoundException("Subscription with ID '" + id + "' was not found in registry."));
    }

    /**
     * Refactored lookup by email returning Optional.
     */
    public Optional<Subscription> findByUserEmail(String email) {
        if (email == null) return Optional.empty();
        for (Subscription sub : subscriptionRepository.values()) {
            if (sub.getUserEmail().equalsIgnoreCase(email.trim())) {
                return Optional.of(sub);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns list of currently active subscriptions (never null).
     */
    public List<Subscription> findActiveSubscriptions(LocalDate asOfDate) {
        List<Subscription> active = new ArrayList<>();
        for (Subscription s : subscriptionRepository.values()) {
            if (s.isActive(asOfDate) || s.isInGracePeriod(asOfDate)) {
                active.add(s);
            }
        }
        return Collections.unmodifiableList(active);
    }
}
