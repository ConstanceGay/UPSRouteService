package Interface;

import Application.Main;
import UPSRouteService.Coordinate;
import UPSRouteService.GPSPoint;
import Utilities.Scanner;
import Utilities.TopCode;
import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

public class UPSCameraPanel extends JPanel {

    private Webcam webcam = Webcam.getWebcams().get(Webcam.getWebcams().size() - 1);
    private GraphicsPoint coordinateX = new GraphicsPoint();
    private GraphicsPoint coordinateY = new GraphicsPoint();
    private GraphicsPoint coordinateOrigin = new GraphicsPoint();
    private GraphicsPoint explorationPoint= new GraphicsPoint();
    private double mapWidth;
    private double mapHeight;

    UPSCameraPanel() {
        super();
        this.setPreferredSize(new Dimension(1600, 896));

        Dimension[] nonStandardResolutions = new Dimension[] {
                new Dimension(1600, 896)
        };

        webcam.setCustomViewSizes(nonStandardResolutions);
        webcam.setViewSize(nonStandardResolutions[0].getSize());
        webcam.open();

        int delay = 2000; // milliseconds
        ActionListener taskPerformer = e -> this.repaint();
        new Timer(delay, taskPerformer).start();
    }

    public void stopCamera(){
        if (webcam.isOpen()){
            webcam.close();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        BufferedImage image = webcam.getImage();
        g.drawImage(image, 0, 0, this);

        g.setColor(Color.RED);

        Scanner scanner = new Scanner(System.in);
        List<TopCode> list = scanner.scan(image);
        list.forEach(t -> {
            switch (t.getCode()) {
                case 31:
                    coordinateY.setCol((int)t.getCenterX());
                    coordinateY.setRow((int)t.getCenterY());
                    g.drawString("Y: " + coordinateY.toString(), coordinateY.getCol() - 20, coordinateY.getRow() - 10);
                    break;
                case 61:
                    coordinateOrigin.setCol((int)t.getCenterX());
                    coordinateOrigin.setRow((int)t.getCenterY());
                    g.drawString("O: " + coordinateOrigin.toString(),  coordinateOrigin.getCol() - 20, coordinateOrigin.getRow() - 10);
                    break;
                case 47:
                    coordinateX.setCol((int)t.getCenterX());
                    coordinateX.setRow((int)t.getCenterY());
                    g.drawString("X: " + coordinateX.toString(), coordinateX.getCol() - 20, coordinateX.getRow() - 10);
                    break;
                case 87:
                    GraphicsPoint topCodePosition = new GraphicsPoint((int)t.getCenterX(), (int)t.getCenterY());
                    GraphicsPoint topCodeOnMap = getCoordinateOnMap(t);
                    GPSPoint gpsPoint = getGpsFromCoordinateOnMap(topCodeOnMap);

                    g.drawString(topCodeOnMap + " -> " + gpsPoint, topCodePosition.getCol() - 20, topCodePosition.getRow() - 10);

                    //System.out.println("GPS Départ : "+gpsPoint.toString());
                    Main.getMapWindow().getMapPanel().setMobileStartPointToDraw(gpsPoint);
                    break;

                case 55:
                    GraphicsPoint topCodePosition2 = new GraphicsPoint((int)t.getCenterX(), (int)t.getCenterY());
                    GraphicsPoint topCodeOnMap2 = getCoordinateOnMap(t);
                    GPSPoint gpsPoint2 = getGpsFromCoordinateOnMap(topCodeOnMap2);

                    g.drawString(topCodeOnMap2 + " -> " + gpsPoint2, topCodePosition2.getCol() - 20, topCodePosition2.getRow() - 10);

                    //System.out.println("GPS Arrivée : "+gpsPoint2.toString());
                    Main.getMapWindow().getMapPanel().setMobileEndPointToDraw(gpsPoint2);
                    break;

                case 59:
                    GraphicsPoint topCodePosition3 = new GraphicsPoint((int) t.getCenterX(), (int) t.getCenterY());
                    GraphicsPoint topCodeOnMap3 = getCoordinateOnMap(t);
                    GPSPoint gpsPoint3 = getGpsFromCoordinateOnMap(topCodeOnMap3);

                    g.drawString(topCodeOnMap3 + " -> " + gpsPoint3, topCodePosition3.getCol() - 20, topCodePosition3.getRow() - 10);

                    if (explorationPoint.getDistanceFrom(topCodeOnMap3) >= 13) {             //if the exploration point moved 1 cm, it is updated
                        explorationPoint = topCodeOnMap3;
                        Main.getMapWindow().getMapPanel().setExplorationPoint(gpsPoint3);
                    }

                    break;

            }
            g.drawOval((int)t.getCenterX() - 5, (int)t.getCenterY() - 5, 10, 10);
        });
    }

    private GraphicsPoint getCoordinateOnMap(TopCode topCode) {
        Coordinate point = new Coordinate(topCode.getCenterX(), topCode.getCenterY());
        Coordinate originToX = new Coordinate(coordinateX.getCol() - coordinateOrigin.getCol(),
                coordinateX.getRow() - coordinateOrigin.getRow());
        Coordinate originToY = new Coordinate(coordinateY.getCol() - coordinateOrigin.getCol(),
                coordinateY.getRow() - coordinateOrigin.getRow());

        double tx = (originToX.getX() * (point.getX() - coordinateOrigin.getCol())
                + originToX.getY() * (point.getY() - coordinateOrigin.getRow())) / originToX.getSquaredSum();
        double ty = (originToY.getX() * (point.getX() - coordinateOrigin.getCol())
                + originToY.getY() * (point.getY() - coordinateOrigin.getRow())) / originToY.getSquaredSum();

        mapWidth = coordinateOrigin.getDistanceFrom(coordinateX);
        mapHeight = coordinateOrigin.getDistanceFrom(coordinateY);

        return new GraphicsPoint((int)(tx * mapWidth), (int)(ty * mapHeight));
    }

    private GPSPoint getGpsFromCoordinateOnMap(GraphicsPoint coordinate) {
        GPSPoint gpsDownRight = Main.getMapWindow().getMapPanel().getGpsDownRight();
        GPSPoint gpsDownLeft = Main.getMapWindow().getMapPanel().getGpsDownLeft();
        GPSPoint gpsUpLeft = Main.getMapWindow().getMapPanel().getGpsUpLeft();

        GPSPoint GPS_BG_BD = new GPSPoint(gpsDownRight.getLongitude() - gpsDownLeft.getLongitude(), gpsDownRight.getLatitude() - gpsDownLeft.getLatitude());
        GPSPoint GPS_BG_HG = new GPSPoint(gpsUpLeft.getLongitude() - gpsDownLeft.getLongitude(), gpsUpLeft.getLatitude() - gpsDownLeft.getLatitude());

        // calculer des proportions pour retrouver les tx et ty entre 0 et 1
        double tx = coordinate.getCol() / coordinateOrigin.getDistanceFrom(coordinateX);
        double ty = coordinate.getRow() / coordinateOrigin.getDistanceFrom(coordinateY);

        // appliquer tx et ty aux coordonnées GPS
        return new GPSPoint(gpsDownLeft.getLongitude() + tx * GPS_BG_BD.getLongitude() + ty * GPS_BG_HG.getLongitude(),
                gpsDownLeft.getLatitude() + tx * GPS_BG_BD.getLatitude() + ty * GPS_BG_HG.getLatitude());
    }

    public double getMapWidth() {
        return mapWidth;
    }

    public double getMapHeight() {
        return mapHeight;
    }

}
