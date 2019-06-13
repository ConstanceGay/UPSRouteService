package UPSRouteService;

import Application.Main;

public class Vecteur2D {

    private double x;
    private double y;
    private static final Vecteur2D GPS_BG = new Vecteur2D(1.460408, 43.555438);
    private static final Vecteur2D GPS_BD = new Vecteur2D(1.480020, 43.555438);
    private static final Vecteur2D GPS_HG = new Vecteur2D(1.460408, 43.565650);
    private static final Vecteur2D GPS_BG_BD = new Vecteur2D(GPS_BD.x - GPS_BG.x, GPS_BD.y - GPS_BG.y);
    private static final Vecteur2D GPS_BG_HG = new Vecteur2D(GPS_HG.x - GPS_BG.x, GPS_HG.y - GPS_BG.y);
    private static final Vecteur2D HG = new Vecteur2D(0, 0);

    public Vecteur2D() {
        x = 0.0;
        y = 0.0;
    }

    public Vecteur2D(double dx, double dy) {
        x = dx;
        y = dy;
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
        int width = Main.getWindow().getWidth();
        int height = Main.getWindow().getHeight();

        // calculer des proportions pour retrouver les tx et ty entre 0 et 1
        double tx = x / width;
        double ty = 1.0 - y / height;
        // appliquer tx et ty aux coordonnées GPS
        return new Vecteur2D(GPS_BG.x + tx * GPS_BG_BD.x, GPS_BG.y + ty * GPS_BG_HG.y);
    }

    public Vecteur2D gps2vue() {
        int width = Main.getWindow().getWidth();
        int height = Main.getWindow().getHeight();
        Vecteur2D BG = new Vecteur2D(0, height);
        Vecteur2D BD = new Vecteur2D(width, height);
        Vecteur2D BG_BD = new Vecteur2D(BD.x - BG.x, BD.y - BG.y);
        Vecteur2D BG_HG = new Vecteur2D(HG.x - BG.x, HG.y - BG.y);

        double tx = (GPS_BG_BD.x*(x - GPS_BG.x) + GPS_BG_BD.y*(y - GPS_BG.y)) / GPS_BG_BD.sommeCarres();
        double ty = (GPS_BG_HG.x*(x - GPS_BG.x) + GPS_BG_HG.y*(y - GPS_BG.y)) / GPS_BG_HG.sommeCarres();

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