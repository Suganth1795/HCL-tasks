package com.hcl.generics.resource;

/**
 * Custom AutoCloseable resource demonstrating deterministic cleanup
 * and LIFO (Last-In, First-Out / Reverse declaration) close order in try-with-resources.
 */
public class CustomResource implements AutoCloseable {

    private final String resourceName;

    public CustomResource(String resourceName) {
        this.resourceName = resourceName;
        System.out.printf("  🟢 [OPENED] Resource: '%s'\n", resourceName);
    }

    public void doWork() {
        System.out.printf("  ⚡ [WORKING] Executing transactional operations on '%s'\n", resourceName);
    }

    @Override
    public void close() {
        System.out.printf("  🔴 [CLOSED] Resource: '%s' has been safely finalized and released.\n", resourceName);
    }

    @Override
    public String toString() {
        return "CustomResource{" + "name='" + resourceName + '\'' + '}';
    }
}
