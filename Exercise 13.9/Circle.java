public class Circle extends GeometricObject
        implements Comparable<Circle> {

    private double radius;

    public Circle() {
    }

    public Circle(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    /** Return area */
    @Override
    public double getArea() {
        return radius * radius * Math.PI;
    }

    /** Return diameter */
    public double getDiameter() {
        return 2 * radius;
    }

    /** Return perimeter */
    @Override
    public double getPerimeter() {
        return 2 * radius * Math.PI;
    }

    /** Compare circles by radius */
    @Override
    public int compareTo(Circle o) {
        return Double.compare(this.radius, o.radius);
    }

    /** Two circles are equal if their radii are equal */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Circle)) return false;
        Circle circle = (Circle) o;
        return Double.compare(radius, circle.radius) == 0;
    }

    /** Print circle info */
    public void printCircle() {
        System.out.println("The circle is created " +
                getDateCreated() + " and the radius is " + radius);
    }
}
