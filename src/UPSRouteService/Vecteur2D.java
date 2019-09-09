package UPSRouteService;

import Application.Main;

public class Vecteur2D {

    private double x;
    private double y;
    private Coordinate GPS_BG = new Coordinate(1.461643,43.562777);
    private Coordinate GPS_BD = new Coordinate(1.468724,43.555375);
    private Coordinate GPS_HG = new Coordinate(1.468209,43.566174);

    private Vecteur2D(double dx, double dy) {
        x = dx;
        y = dy;
    }

    public Vecteur2D(double dx, double dy, GPSPoint gpsDownLeft, GPSPoint gpsDownRight, GPSPoint gpsUpLeft) {
        x = dx;
        y = dy;
        GPS_BD = new Coordinate(gpsDownRight.getLongitude(), gpsDownRight.getLatitude());
        GPS_BG = new Coordinate(gpsDownLeft.getLongitude(), gpsDownLeft.getLatitude());
        GPS_HG = new Coordinate(gpsUpLeft.getLongitude(), gpsUpLeft.getLatitude());
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