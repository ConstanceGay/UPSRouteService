package UPSRouteService;

import Application.Main;
import Interface.GraphicsPoint;

import java.io.Serializable;

public class GPSPoint implements Serializable {

    private double latitude;
    private double longitude;

    public GPSPoint(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public GraphicsPoint getGraphicsPoint() {
        GPSPoint gpsUpLeft = Main.getMapWindow().getMapPanel().getGpsUpLeft();
        GPSPoint gpsDownLeft = Main.getMapWindow().getMapPanel().getGpsDownLeft();
        GPSPoint gpsDownRight = Main.getMapWindow().getMapPanel().getGpsDownRight();

        GPSPoint gpsDownLeftToDownRight = new GPSPoint(gpsDownRight.longitude - gpsDownLeft.longitude, gpsDownRight.latitude - gpsDownLeft.latitude);
        GPSPoint gpsDownLeftToUpLeft = new GPSPoint(gpsUpLeft.longitude - gpsDownLeft.longitude, gpsUpLeft.latitude - gpsDownLeft.latitude);
        GPSPoint W = new GPSPoint(this.longitude - gpsDownLeft.getLongitude(), this.latitude - gpsDownLeft.getLatitude());

        double A = gpsDownLeftToUpLeft.getSquaredSum();
        double D = gpsDownLeftToDownRight.getSquaredSum();
        double B = ((gpsDownLeftToDownRight.getLongitude() * gpsDownLeftToUpLeft.getLongitude()) + (gpsDownLeftToDownRight.getLatitude() * gpsDownLeftToUpLeft.getLatitude()));
        double det = (A * D) - (B * B);

        B = -1 * B;
        det = 1 / det;

        double tx = det * ((A * gpsDownLeftToDownRight.getLongitude() + B * gpsDownLeftToUpLeft.getLongitude()) * W.longitude
                + (A * gpsDownLeftToDownRight.getLatitude() + B * gpsDownLeftToUpLeft.getLatitude()) * W.latitude);
        double ty = det * ((B * gpsDownLeftToDownRight.getLongitude() + D * gpsDownLeftToUpLeft.getLongitude()) * W.longitude
                + (B * gpsDownLeftToDownRight.getLatitude() + D * gpsDownLeftToUpLeft.getLatitude()) * W.latitude);

        return new GraphicsPoint((int)(tx * Main.getMapWindow().getMapPanel().getScaleWidth()), (int)((1-ty) * Main.getMapWindow().getHeight()));

         /*double tx = (gpsDownLeftToDownRight.longitude * (longitude - gpsDownLeft.longitude)
                + gpsDownLeftToDownRight.latitude * (latitude - gpsDownLeft.latitude))
                / gpsDownLeftToDownRight.getSquaredSum();

        double ty = (gpsDownLeftToUpLeft.longitude * (longitude - gpsDownLeft.longitude)
                + gpsDownLeftToUpLeft.latitude * (latitude - gpsDownLeft.latitude))
                / gpsDownLeftToUpLeft.getSquaredSum();

        System.out.print("[tx: " + tx + ", ty: " + ty + "] -> ");

        //GraphicsPoint graphicsPoint = new GraphicsPoint((int)(gpsDownLeft.longitude + tx * gpsDownLeftToDownRight.longitude),
                //(int)(gpsDownLeft.latitude + ty * gpsDownLeftToUpLeft.latitude));

        GraphicsPoint graphicsPoint = new GraphicsPoint((int)(tx * Main.getMapWindow().getMapPanel().getScaleWidth()),
                (int)(ty * Main.getMapWindow().getMapPanel().getHeight()));

        System.out.println(graphicsPoint);

        return graphicsPoint;*/
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    private double getSquaredSum() {
        return longitude * longitude + latitude * latitude;
    }

    public String toString() {
        return "[lon: " + longitude + ", lat: " + latitude + "]";
    }

}
