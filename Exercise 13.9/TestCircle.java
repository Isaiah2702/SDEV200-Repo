public class TestCircle {
    public static void main(String[] args) {
        Circle c1 = new Circle(5);
        Circle c2 = new Circle(5);
        Circle c3 = new Circle(7);

        System.out.println("c1 equals c2? " + c1.equals(c2));
        System.out.println("c1 compareTo c3: " + c1.compareTo(c3));
        c1.printCircle();
    }
}
