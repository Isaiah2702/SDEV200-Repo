public class RegularPolygon {
    private int n = 3;
    private double side = 1.0;
    private double x = 0.0;
    private double y = 0.0;

    /** No-arg constructor with default values */
    public RegularPolygon() {
    }

    /** Constructor with n sides, side length; centered at (0,0) */
    public RegularPolygon(int n, double side) {
        this.n = n;
        this.side = side;
        this.x = 0.0;
        this.y = 0.0;
    }

    /** Constructor with n sides, side length; centered at (x,y) */
    public RegularPolygon(int n, double side, double x, double y) {
        this.n = n;
        this.side = side;
        this.x = x;
        this.y = y;
    }

    // Accessors (getters)
    public int getN() {
        return n;
    }

    public double getSide() {
        return side;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Mutators (setters)
    public void setN(int n) {
        this.n = n;
    }

    public void setSide(double side) {
        this.side = side;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /** Return the perimeter of the polygon */
    public double getPerimeter() {
        return n * side;
    }

    /**
     * Return the area of the polygon
     * Area = (n * s^2) / (4 * tan(pi/n))
     */
    public double getArea() {
        return (n * side * side) / (4.0 * Math.tan(Math.PI / n));
    }
}
