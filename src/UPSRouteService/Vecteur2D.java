package UPSRouteService;

import Application.Main;

public class Vecteur2D {

    private double x;
    private double y;
    private Coordinate GPS_BG = new Coordinate(1.460408, 43.555438);
    private Coordinate GPS_BD = new Coordinate(1.480020, 43.555438);
    private Coordinate GPS_HG = new Coordinate(1.460408, 43.565650);
    private static final Vecteur2D HG = new Vecteur2D(0, 0);

    public Vecteur2D() {
        x = 0.0;
        y = 0.0;
    }

    public Vecteur2D(double dx, double dy) {
        x = dx;
        y = dy;
    }

    public Vecteur2D(double dx, double dy, Coordinate gpsDownLeft, Coordinate gpsDownRight, Coordinate gpsUpLeft) {
        x = dx;
        y = dy;
        GPS_BD = gpsDownRight;
        GPS_BG = gpsDownLeft;
        GPS_HG = gpsUpLeft;
    }

    public double norme() {
        return (Math.sqrt(x*x + y*y));
    }

    private double sommeCarres() {
        return (x*x + y*y);
    }

    public Vecteur2D normer() {
        double norme = Math.sqrt(sommeCarres());
        return new Vecteur2D(x / norme, y / norme);
    }

    public double produitScalaire(Vecteur2D v) {
        return (x * v.x) + (y * v.y);
    }

    public  Vecteur2D vue2gps() {
        int width = Main.getMapWindow().getWidth();
        int height = Main.getMapWindow().getHeight();
        Coordinate GPS_BG_BD = new Coordinate(GPS_BD.getX() - GPS_BG.getX(), GPS_BD.getY() - GPS_BG.getY());
        Coordinate GPS_BG_HG = new Coordinate(GPS_HG.getX() - GPS_BG.getX(), GPS_HG.getY() - GPS_BG.getY());

        // calculer des proportions pour retrouver les tx et ty entre 0 et 1
        double tx = x / width;
        double ty = 1.0 - y / height;
        // appliquer tx et ty aux coordonnées GPS
        return new Vecteur2D(GPS_BG.getX() + tx * GPS_BG_BD.getX(), GPS_BG.getY() + ty * GPS_BG_HG.getY());
    }

    public Vecteur2D gps2vue() {
        int width = Main.getMapWindow().getWidth();
        int height = Main.getMapWindow().getHeight();
        Vecteur2D BG = new Vecteur2D(0, height);
        Vecteur2D BD = new Vecteur2D(width, height);
        Vecteur2D BG_BD = new Vecteur2D(BD.x - BG.x, BD.y - BG.y);
        Vecteur2D BG_HG = new Vecteur2D(HG.x - BG.x, HG.y - BG.y);

        Coordinate GPS_BG_BD = new Coordinate(GPS_BD.getX() - GPS_BG.getX(), GPS_BD.getY() - GPS_BG.getY());
        Coordinate GPS_BG_HG = new Coordinate(GPS_HG.getX() - GPS_BG.getX(), GPS_HG.getY() - GPS_BG.getY());

        double tx = (GPS_BG_BD.getX() * (x - GPS_BG.getX()) + GPS_BG_BD.getY() * (y - GPS_BG.getY())) / GPS_BG_BD.squaredSum();
        double ty = (GPS_BG_HG.getX() * (x - GPS_BG.getX()) + GPS_BG_HG.getY() * (y - GPS_BG.getY())) / GPS_BG_HG.squaredSum();

        Vecteur2D pt =  new Vecteur2D();
        pt.x = BG.x + tx * BG_BD.x;
        pt.y = BG.y + ty * BG_HG.y;

        return pt;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String toString() {
        return "["+x+", "+y+"]";
    }
}