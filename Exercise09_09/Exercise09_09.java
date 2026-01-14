public class Exercise09_09 {

    public static void main(String[] args) {
        RegularPolygon p1 = new RegularPolygon();
        RegularPolygon p2 = new RegularPolygon(6, 4);
        RegularPolygon p3 = new RegularPolygon(10, 4, 5.6, 7.8);

        printPolygon("Polygon 1", p1);
        printPolygon("Polygon 2", p2);
        printPolygon("Polygon 3", p3);
    }

    private static void printPolygon(String label, RegularPolygon p) {
        System.out.println(label + ":");
        System.out.printf("Perimeter: %.2f%n", p.getPerimeter());
        System.out.printf("Area: %.2f%n%n", p.getArea());
    }
}
