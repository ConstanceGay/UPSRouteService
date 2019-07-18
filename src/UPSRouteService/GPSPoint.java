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

        return new GraphicsPoint((int)(tx * Main.getMapWindow().getMapPanel().getScaleWidth()), (int)((1-ty) * Main.getMapWindow().getMapPanel().getHeight()));
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
