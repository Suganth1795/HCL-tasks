public class PlatformInfo {

    public static void main(String[] args) {

        Runtime runtime = Runtime.getRuntime();

        System.out.println("Java Version   : " + System.getProperty("java.version"));
        System.out.println("OS Name        : " + System.getProperty("os.name"));
        System.out.println("OS Architecture: " + System.getProperty("os.arch"));
        System.out.println("Processors      : " + runtime.availableProcessors());
        System.out.println("Max Memory     : " + runtime.maxMemory());
        System.out.println("Free Memory    : " + runtime.freeMemory());
    }
}