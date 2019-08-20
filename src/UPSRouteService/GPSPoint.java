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

    //FUNCTIONS TO CALCULATE DISTANCE BETWEEN TWO GPS POINTS

    public double distanceGPSPoint (GPSPoint gpsPoint) {
        double theta = this.longitude - gpsPoint.getLongitude();
        double dist = Math.sin(deg2rad(this.latitude)) * Math.sin(deg2rad(gpsPoint.getLatitude())) + Math.cos(deg2rad(this.latitude)) * Math.cos(deg2rad(gpsPoint.getLatitude())) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344 * 1000;             //converts to m

        return dist;
    }

    private double deg2rad(double deg) { return (deg * Math.PI / 180.0); }

    private double rad2deg(double rad) { return (rad * 180.0 / Math.PI); }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  GETTERS            :*/
    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  SETTERS            :*/

    public void setLatitude(double latitude){ this.latitude = latitude; }

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
