package UPSRouteService;

import java.io.Serializable;

public class Coordinate implements Serializable {

    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public double getSquaredSum() {
        return x * x + y * y;
    }

    public String toString() {
        return "[" + x + ", " + y + "]";
    }

}
