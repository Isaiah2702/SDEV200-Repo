public class Exercise06_09 {

    /** Convert from feet to meters */
    public static double footToMeter(double foot) {
        return 0.305 * foot;
    }

    /** Convert from meters to feet */
    public static double meterToFoot(double meter) {
        return 3.279 * meter;
    }

    public static void main(String[] args) {

        // Table headers
        System.out.printf("%-10s%-10s    %-10s%-10s%n",
                "Feet", "Meters", "Meters", "Feet");
        System.out.println("-----------------------------------------------");

        double feet = 1.0;
        double meters = 20.0;

        // Print table rows
        while (feet <= 10.0) {
            System.out.printf("%-10.1f%-10.3f    %-10.1f%-10.3f%n",
                    feet,
                    footToMeter(feet),
                    meters,
                    meterToFoot(meters));

            feet++;
            meters += 5;
        }
    }
}
