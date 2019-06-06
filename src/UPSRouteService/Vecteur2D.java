package UPSRouteService;

public class Vecteur2D {

    protected double x;
    protected double y;

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
        double norme  = Math.sqrt(sommeCarres());
        return new Vecteur2D(x / norme, y / norme);
    }

    public double produitScalaire(Vecteur2D v) {
        return (x * v.x) + (y * v.y);
    }

    /*public  Vecteur2D vue2gps() {
        // calculer des proportions pour retrouver les tx et ty entre 0 et 1
        double tx = x / WIDTH;
        double ty = 1.0 - y / HEIGHT;
        // appliquer tx et ty aux coordonnées GPS
        return new Vecteur2D(GPS_BG.x + tx * GPS_BG_BD.x, GPS_BG.y + ty * GPS_BG_HG.y);
    }

    public Vecteur2D gps2vue() {
        Vecteur2D pt =  new Vecteur2D();
        double tx = (GPS_BG_BD.x*(x - GPS_BG.x) + GPS_BG_BD.y*(y - GPS_BG.y)) / GPS_BG_BD.sommeCarres();
        double ty = (GPS_BG_HG.x*(x - GPS_BG.x) + GPS_BG_HG.y*(y - GPS_BG.y)) / GPS_BG_HG.sommeCarres();

        pt.x = BG.x + tx * BG_BD.x;
        pt.y = BG.y + ty * BG_HG.y;
        if (DEBUG)
            println("gps2vue pt : "+ pt.toString()+ " tx "+tx + " ty "+ty);
        return pt;
    }*/

    public String toString() {
        return "["+x+", "+y+"]";
    }
}