package Interface;

import Application.Main;
import UPSRouteService.Coordinate;
import UPSRouteService.UPSRouteService;
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
    private Coordinate coordinateX = new Coordinate();
    private Coordinate coordinateY = new Coordinate();
    private Coordinate coordinateOrigin = new Coordinate();
    private UPSRouteService upsRouteService = new UPSRouteService();

    UPSCameraPanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));

        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();

        int delay = 500; // milliseconds
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
                    coordinateX.setX(t.getCenterX());
                    coordinateX.setY(t.getCenterY());
                    break;
                case 61:
                    coordinateOrigin.setX(t.getCenterX());
                    coordinateOrigin.setY(t.getCenterY());
                    break;
                case 47:
                    coordinateY.setX(t.getCenterX());
                    coordinateY.setY(t.getCenterY());
                    break;
                case 87:
                    Coordinate coordinate = getCoordinateOnMap(t);
                    System.out.print(coordinate + " -> ");

                    coordinate = getGpsFromCoordinateOnMap(coordinate);
                    System.out.print(coordinate + " -> ");

                    System.out.println(upsRouteService.getBuilding(coordinate.getX(), coordinate.getY()));
                    break;
            }
            g.drawOval((int)t.getCenterX() - 5, (int)t.getCenterY() - 5, 10, 10);
        });
    }

    private Coordinate getCoordinateOnMap(TopCode topCode) {
        Coordinate point = new Coordinate(topCode.getCenterX(), topCode.getCenterY());
        Coordinate originToX = new Coordinate(coordinateX.getX() - coordinateOrigin.getX(), coordinateX.getY() - coordinateOrigin.getY());
        Coordinate originToY = new Coordinate(coordinateY.getX() - coordinateOrigin.getX(), coordinateY.getY() - coordinateOrigin.getY());

        double tx = (originToX.getX() * (point.getX() - coordinateOrigin.getX())
                + originToX.getY() * (point.getY() - coordinateOrigin.getY())) / originToX.getSquaredSum();
        double ty = (originToY.getX() * (point.getX() - coordinateOrigin.getX())
                + originToY.getY() * (point.getY() - coordinateOrigin.getY())) / originToY.getSquaredSum();

        return new Coordinate(tx * this.getWidth(), ty * this.getHeight());
    }

    private Coordinate getGpsFromCoordinateOnMap(Coordinate coordinate) {
        Coordinate gpsDownRight = Main.getMapWindow().getMapPanel().getGpsDownRight();
        Coordinate gpsDownLeft = Main.getMapWindow().getMapPanel().getGpsDownLeft();
        Coordinate gpsUpLeft = Main.getMapWindow().getMapPanel().getGpsUpLeft();

        Coordinate GPS_BG_BD = new Coordinate(gpsDownRight.getX() - gpsDownLeft.getX(), gpsDownRight.getY() - gpsDownLeft.getY());
        Coordinate GPS_BG_HG = new Coordinate(gpsUpLeft.getX() - gpsDownLeft.getX(), gpsUpLeft.getY() - gpsDownLeft.getY());

        // calculer des proportions pour retrouver les tx et ty entre 0 et 1
        double tx = coordinate.getX() / Main.getMapWindow().getMapPanel().getWidth();
        double ty = 1.0 - coordinate.getY() / Main.getMapWindow().getMapPanel().getHeight();

        // appliquer tx et ty aux coordonnées GPS
        return new Coordinate(gpsDownLeft.getX() + tx * GPS_BG_BD.getX() + ty * GPS_BG_HG.getX(),
                gpsDownLeft.getY() + tx * GPS_BG_BD.getY() + ty * GPS_BG_HG.getY());
    }

}
