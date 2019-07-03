package Interface;

import Application.Main;
import UPSRouteService.Coordinate;
import UPSRouteService.UPSRouteService;
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

    private Webcam webcam = Webcam.getWebcams().get(1);
    private GraphicsPoint coordinateX = new GraphicsPoint();
    private GraphicsPoint coordinateY = new GraphicsPoint();
    private GraphicsPoint coordinateOrigin = new GraphicsPoint();
    private UPSRouteService upsRouteService = new UPSRouteService();
    private double mapWidth;
    private double mapHeight;

    UPSCameraPanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));

        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();

        int delay = 2000; // milliseconds
        ActionListener taskPerformer = e -> this.repaint();
        new Timer(delay, taskPerformer).start();
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

                    System.out.println(gpsPoint);
                    g.drawString(topCodeOnMap + " -> " + gpsPoint, topCodePosition.getCol() - 20, topCodePosition.getRow() - 10);

                    //System.out.println(upsRouteService.getBuilding(coordinate.getX(), coordinate.getY()));
                    Main.getMapWindow().getMapPanel().setMobilePointToDraw(gpsPoint);
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
        Coordinate gpsDownRight = Main.getMapWindow().getMapPanel().getGpsDownRight();
        Coordinate gpsDownLeft = Main.getMapWindow().getMapPanel().getGpsDownLeft();
        Coordinate gpsUpLeft = Main.getMapWindow().getMapPanel().getGpsUpLeft();

        Coordinate GPS_BG_BD = new Coordinate(gpsDownRight.getX() - gpsDownLeft.getX(), gpsDownRight.getY() - gpsDownLeft.getY());
        Coordinate GPS_BG_HG = new Coordinate(gpsUpLeft.getX() - gpsDownLeft.getX(), gpsUpLeft.getY() - gpsDownLeft.getY());

        // calculer des proportions pour retrouver les tx et ty entre 0 et 1
        double tx = coordinate.getCol() / coordinateOrigin.getDistanceFrom(coordinateX);
        double ty = coordinate.getRow() / coordinateOrigin.getDistanceFrom(coordinateY);

        // appliquer tx et ty aux coordonnées GPS
        return new GPSPoint(gpsDownLeft.getX() + tx * GPS_BG_BD.getX() + ty * GPS_BG_HG.getX(),
                gpsDownLeft.getY() + tx * GPS_BG_BD.getY() + ty * GPS_BG_HG.getY());
    }

    public double getMapWidth() {
        return mapWidth;
    }

    public double getMapHeight() {
        return mapHeight;
    }

}
