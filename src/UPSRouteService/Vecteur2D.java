package UPSRouteService;

import Application.Main;

public class Vecteur2D {

    private double x;
    private double y;
    private Coordinate GPS_BG = new Coordinate(1.460408, 43.555438);
    private Coordinate GPS_BD = new Coordinate(1.480020, 43.555438);
    private Coordinate GPS_HG = new Coordinate(1.46726, 43.565650);
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
        Coordinate GPS_BG_BD = new Coordinate(GPS_BD.getX() - GPS_BG.getX(), GPS_BD.getY() - GPS_BG.getY());
        Coordinate GPS_BG_HG = new Coordinate(GPS_HG.getX() - GPS_BG.getX(), GPS_HG.getY() - GPS_BG.getY());

        // calculer des proportions pour retrouver les tx et ty entre 0 et 1
        double tx = x / Main.getMapWindow().getMapPanel().getWidth();
        double ty = 1.0 - y / Main.getMapWindow().getMapPanel().getHeight();
        // appliquer tx et ty aux coordonnées GPS
        return new Vecteur2D(GPS_BG.getX() + tx * GPS_BG_BD.getX() + ty * GPS_BG_HG.getX(),
                GPS_BG.getY() + tx * GPS_BG_BD.getY() + ty * GPS_BG_HG.getY());
    }

    public Vecteur2D gps2vue() {
        Coordinate GPS_BG_BD = new Coordinate(GPS_BD.getX() - GPS_BG.getX(), GPS_BD.getY() - GPS_BG.getY());
        Coordinate GPS_BG_HG = new Coordinate(GPS_HG.getX() - GPS_BG.getX(), GPS_HG.getY() - GPS_BG.getY());

        Vecteur2D W = new Vecteur2D(this.x - GPS_BG.getX(), this.y - GPS_BG.getY());
        Vecteur2D pt =  new Vecteur2D();

        double A = GPS_BG_HG.getSquaredSum();
        double D = GPS_BG_BD.getSquaredSum();
        double B = ((GPS_BG_BD.getX() * GPS_BG_HG.getX()) + (GPS_BG_BD.getY() * GPS_BG_HG.getY()));
        double det = (A * D) - (B * B);

        B = -1*B;
        det = 1/det;

        double tx = det * ((A*GPS_BG_BD.getX() + B*GPS_BG_HG.getX())*W.x + (A*GPS_BG_BD.getY() + B*GPS_BG_HG.getY())*W.y);
        double ty = det * ((B*GPS_BG_BD.getX() + D*GPS_BG_HG.getX())*W.x + (B*GPS_BG_BD.getY() + D*GPS_BG_HG.getY())*W.y);

        pt.x = tx * Main.getMapWindow().getMapPanel().getScaleWidth();
        pt.y = (1-ty) * Main.getMapWindow().getHeight();

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