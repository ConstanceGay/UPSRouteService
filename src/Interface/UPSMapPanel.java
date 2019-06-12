package Interface;

import UPSRouteService.Location;
import UPSRouteService.UPSRouteService;
import UPSRouteService.Vecteur2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class UPSMapPanel extends JPanel {

    private Location start;
    private Location end;
    private List<Vecteur2D> coordinates;

    UPSMapPanel(Location start, Location end) {
        this.start = start;
        this.end = end;

        UPSRouteService upsRouteService = new UPSRouteService();
        coordinates = upsRouteService.getRoute(start, end).getCoordinates();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        try {
            BufferedImage image = ImageIO.read(new File("img/ups-map.jpg"));
            double scaleFactor = Math.min(1d, getScaleFactorToFit(new Dimension(image.getWidth(), image.getHeight()), getSize()));

            int scaleWidth = (int) Math.round(image.getWidth() * scaleFactor);
            int scaleHeight = (int) Math.round(image.getHeight() * scaleFactor);

            Image scaled = image.getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_SMOOTH);

            int width = getWidth() - 1;
            int height = getHeight() - 1;

            int x = (width - scaled.getWidth(this)) / 2;
            int y = (height - scaled.getHeight(this)) / 2;

            g.drawImage(scaled, x, y, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.setColor(Color.BLUE);
        g.drawOval((int)coordinates.get(0).gps2vue().getX() - 2, (int)coordinates.get(0).gps2vue().getY() - 2, 4, 4);
        g.drawString(start.toString(), (int)coordinates.get(0).gps2vue().getX() - 2, (int)coordinates.get(0).gps2vue().getY() - 2);

        for (int i = 0; i < coordinates.size() - 1; i++) {
            g.setColor(Color.RED);
            g.drawLine((int)coordinates.get(i).gps2vue().getX(), (int)coordinates.get(i).gps2vue().getY(), (int)coordinates.get(i + 1).gps2vue().getX(), (int)coordinates.get(i + 1).gps2vue().getY());
        }

        g.setColor(Color.BLUE);
        g.drawOval((int)coordinates.get(coordinates.size() - 1).gps2vue().getX() - 2, (int)coordinates.get(coordinates.size() - 1).gps2vue().getY() - 2, 4, 4);
        g.drawString(end.toString(), (int)coordinates.get(coordinates.size() - 1).gps2vue().getX() - 2, (int)coordinates.get(coordinates.size() - 1).gps2vue().getY() - 2);

        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(90 * Math.PI / 180);
    }

    private double getScaleFactorToFit(Dimension original, Dimension toFit) {
        double dScale = 1d;

        if (original != null && toFit != null) {

            double dScaleWidth = getScaleFactor(original.width, toFit.width);
            double dScaleHeight = getScaleFactor(original.height, toFit.height);

            dScale = Math.min(dScaleHeight, dScaleWidth);
        }

        return dScale;
    }

    private double getScaleFactor(int iMasterSize, int iTargetSize) {
        double dScale;
        if (iMasterSize > iTargetSize) {
            dScale = (double)iTargetSize / (double)iMasterSize;
        } else {
            dScale = (double)iTargetSize / (double)iMasterSize;
        }

        return dScale;
    }

}
