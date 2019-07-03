package UPSRouteService;

import Application.Main;
import Interface.GraphicsPoint;

public class GPSPoint {

    private double latitude;
    private double longitude;

    public GPSPoint(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public GraphicsPoint getGraphicsPoint(GPSPoint upLeft, GPSPoint downLeft, GPSPoint downRight) {
        GPSPoint gpsDownLeftToDownRight = new GPSPoint(downLeft.getLongitude() - downRight.getLongitude(),
                downLeft.getLatitude() - downRight.getLatitude());
        GPSPoint gpsDownLeftToUpLeft = new GPSPoint(upLeft.getLongitude() - downLeft.getLongitude(),
                upLeft.getLatitude() - downLeft.getLatitude());

        GPSPoint W = new GPSPoint(longitude - downLeft.getLongitude(), latitude - downLeft.getLatitude());

        double A = gpsDownLeftToUpLeft.getSquaredSum();
        double D = gpsDownLeftToDownRight.getSquaredSum();
        double B = ((gpsDownLeftToDownRight.getLongitude() * gpsDownLeftToUpLeft.getLongitude())
                + (gpsDownLeftToDownRight.getLatitude() * gpsDownLeftToUpLeft.getLatitude()));
        double det = (A * D) - (B * B);

        B = -1 * B;
        det = 1 / det;

        double tx = det * ((A * gpsDownLeftToDownRight.getLatitude() + B * gpsDownLeftToUpLeft.getLatitude()) * W.latitude
                + (A * gpsDownLeftToDownRight.getLongitude() + B*gpsDownLeftToUpLeft.getLongitude()) * W.longitude);
        double ty = det * ((B*gpsDownLeftToDownRight.getLatitude() + D * gpsDownLeftToUpLeft.getLatitude()) * W.latitude
                + (B*gpsDownLeftToDownRight.getLongitude() + D*gpsDownLeftToUpLeft.getLongitude()) * W.longitude);

        return new GraphicsPoint((int)(tx * Main.getMapWindow().getWidth()), (int)((1 - ty) * Main.getMapWindow().getHeight()));
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private double getSquaredSum() {
        return longitude * longitude + latitude * latitude;
    }

    public String toString() {
        return "[lat: " + latitude + ", lon: " + longitude + "]";
    }

}
