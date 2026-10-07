/**
 * Daily Task 2: Language Fundamentals
 * Topic: Java Language Fundamentals & Git Fundamentals
 * 
 * Requirements:
 * - 1D int array for 12 months usage data
 * - Slab constants (no magic numbers)
 * - Calculation of Total, Average (with type casting), Max, and Min
 * - Long accumulator to prevent integer overflow
 * - Character grade calculation using nested ternary operator
 * - 2D array representation for multiple houses (3 houses x 12 months)
 */
public class MonthlyUsageAnalyzer {

    // Slab constants for billing and threshold classifications (no magic numbers)
    public static final int MONTHS_IN_YEAR = 12;
    public static final int NUM_HOUSES = 3;

    public static final double SLAB1_RATE = 1.50;  // Up to 100 units
    public static final double SLAB2_RATE = 3.00;  // 101 to 300 units
    public static final double SLAB3_RATE = 5.00;  // Above 300 units

    public static final int SLAB1_LIMIT = 100;
    public static final int SLAB2_LIMIT = 300;

    // Thresholds for efficiency grades
    public static final double GRADE_A_THRESHOLD = 150.0;
    public static final double GRADE_B_THRESHOLD = 300.0;

    public static final String[] MONTH_NAMES = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("         DAILY TASK 2: MONTHLY USAGE ANALYZER                  ");
        System.out.println("===============================================================\n");

        // Part 1: Single House 1-D Array Analysis (12 Months)
        int[] singleHouseUnits = {120, 145, 180, 290, 340, 420, 380, 310, 220, 190, 130, 115};

        System.out.println("--- PART 1: SINGLE HOUSE ANNUAL USAGE ANALYSIS ---");
        analyzeSingleHouse(singleHouseUnits);

        // Part 2: Multi-House 2-D Array Analysis (3 Houses x 12 Months)
        int[][] multiHouseUnits = {
            {120, 145, 180, 290, 340, 420, 380, 310, 220, 190, 130, 115}, // House 1
            {95, 110, 130, 160, 210, 250, 230, 190, 150, 120, 100, 90},   // House 2
            {310, 350, 420, 510, 580, 620, 590, 540, 430, 380, 320, 290}  // House 3
        };

        System.out.println("\n--- PART 2: MULTI-HOUSE (2-D ARRAY) COMPARATIVE ANALYSIS ---");
        analyzeMultiHouse(multiHouseUnits);
    }

    /**
     * Analyzes monthly usage for a single house.
     * Demonstrates: Total with long accumulator, Cast average, Min/Max, Ternary grade, Slab bill calculation.
     */
    public static void analyzeSingleHouse(int[] monthlyUnits) {
        // Use long accumulator to prevent integer overflow
        long totalUnits = 0L;
        int maxUnits = monthlyUnits[0];
        int minUnits = monthlyUnits[0];
        int maxMonthIndex = 0;
        int minMonthIndex = 0;

        for (int i = 0; i < monthlyUnits.length; i++) {
            int units = monthlyUnits[i];
            totalUnits += units; // Accumulating into long

            if (units > maxUnits) {
                maxUnits = units;
                maxMonthIndex = i;
            }
            if (units < minUnits) {
                minUnits = units;
                minMonthIndex = i;
            }
        }

        // Explicit type cast to double for precise average
        double averageUnits = (double) totalUnits / monthlyUnits.length;

        // Efficiency rating assigned using nested ternary operator
        // 'A' -> Efficient (< 150), 'B' -> Moderate (150 - 300), 'C' -> High (> 300)
        char efficiencyGrade = (averageUnits < GRADE_A_THRESHOLD) ? 'A' :
                               (averageUnits <= GRADE_B_THRESHOLD) ? 'B' : 'C';

        double estimatedAnnualBill = calculateBill(totalUnits);

        // Display results
        System.out.println("Monthly Breakdown:");
        for (int i = 0; i < monthlyUnits.length; i++) {
            System.out.printf("  %-10s : %4d kWh\n", MONTH_NAMES[i], monthlyUnits[i]);
        }
        System.out.println("---------------------------------------------------------------");
        System.out.printf("Total Annual Usage     : %d kWh (Computed using long to prevent overflow)\n", totalUnits);
        System.out.printf("Average Monthly Usage  : %.2f kWh (Explicit cast: (double) total / length)\n", averageUnits);
        System.out.printf("Peak Month (Max)       : %s with %d kWh\n", MONTH_NAMES[maxMonthIndex], maxUnits);
        System.out.printf("Lowest Month (Min)     : %s with %d kWh\n", MONTH_NAMES[minMonthIndex], minUnits);
        System.out.printf("Efficiency Grade       : '%c' (via Ternary: A < %.0f, B <= %.0f, C > %.0f)\n", 
                efficiencyGrade, GRADE_A_THRESHOLD, GRADE_B_THRESHOLD, GRADE_B_THRESHOLD);
        System.out.printf("Estimated Annual Bill  : $%.2f (Based on slab constants)\n", estimatedAnnualBill);
    }

    /**
     * Calculates utility bill based on tiered slab constants without magic numbers.
     */
    public static double calculateBill(long units) {
        double bill = 0.0;
        if (units <= SLAB1_LIMIT) {
            bill = units * SLAB1_RATE;
        } else if (units <= SLAB2_LIMIT) {
            bill = (SLAB1_LIMIT * SLAB1_RATE) + ((units - SLAB1_LIMIT) * SLAB2_RATE);
        } else {
            bill = (SLAB1_LIMIT * SLAB1_RATE) + 
                   ((SLAB2_LIMIT - SLAB1_LIMIT) * SLAB2_RATE) + 
                   ((units - SLAB2_LIMIT) * SLAB3_RATE);
        }
        return bill;
    }

    /**
     * Analyzes 2-D array of 3 houses across 12 months.
     */
    public static void analyzeMultiHouse(int[][] housesData) {
        long grandTotalUnits = 0L;

        System.out.printf("%-10s | %-12s | %-12s | %-10s | %-10s | %-6s\n", 
                "House", "Total Units", "Avg Monthly", "Min Month", "Max Month", "Grade");
        System.out.println("-------------------------------------------------------------------------");

        for (int h = 0; h < housesData.length; h++) {
            long houseTotal = 0L;
            int houseMin = housesData[h][0];
            int houseMax = housesData[h][0];

            for (int m = 0; m < housesData[h].length; m++) {
                int val = housesData[h][m];
                houseTotal += val;
                if (val < houseMin) houseMin = val;
                if (val > houseMax) houseMax = val;
            }

            grandTotalUnits += houseTotal;
            double houseAvg = (double) houseTotal / housesData[h].length;
            char houseGrade = (houseAvg < GRADE_A_THRESHOLD) ? 'A' :
                              (houseAvg <= GRADE_B_THRESHOLD) ? 'B' : 'C';

            System.out.printf("House #%-4d | %8d kWh  | %8.2f kWh  | %6d kWh  | %6d kWh  |   %c  \n",
                    (h + 1), houseTotal, houseAvg, houseMin, houseMax, houseGrade);
        }

        System.out.println("-------------------------------------------------------------------------");
        double communityAvg = (double) grandTotalUnits / (housesData.length * MONTHS_IN_YEAR);
        System.out.printf("Combined Grand Total for %d Houses : %d kWh\n", housesData.length, grandTotalUnits);
        System.out.printf("Overall Community Average Monthly : %.2f kWh\n", communityAvg);
    }
}
