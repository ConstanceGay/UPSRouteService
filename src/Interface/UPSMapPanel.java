package Interface;

import UPSRouteService.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class UPSMapPanel extends JPanel implements MouseListener{

    private List<Vecteur2D> coordinates;
    private UPSRouteService upsRouteService = new UPSRouteService(this);
    private GPSPoint mobileStartPointToDraw;
    private GPSPoint mobileEndPointToDraw;
    private List<Integer> wayPointsToDraw = new LinkedList<>();
    private Path steps;
    private Coordinate gpsDownLeft;
    private Coordinate gpsDownRight;
    private Coordinate gpsUpLeft;
    private Coordinate mouseCoordinate = new Coordinate(0,0);
    private String building = "";
    private int distance;
    private int duration;

    UPSMapPanel(Location start, Location end) {
        addMouseListener(this);
        loadGpsConfig();
        drawRoute(start, end);

        CalibrationWindow calibrationWindow = new CalibrationWindow();
        calibrationWindow.setVisible(true);

        this.setBackground(Color.BLACK);
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        mouseCoordinate.setX(evt.getPoint().x);
        mouseCoordinate.setY(evt.getPoint().y);
        Vecteur2D mouse_coordinates = new Vecteur2D(mouseCoordinate.getX(),mouseCoordinate.getY(),gpsDownLeft,gpsDownRight,gpsUpLeft);
        Coordinate mouse_GPS = new Coordinate (mouse_coordinates.vue2gps().getX(),mouse_coordinates.vue2gps().getY());
        building = upsRouteService.getBuilding(mouse_GPS.getX(),mouse_GPS.getY());
        repaint();
    }

    void setProfile(Profile profile) {
        upsRouteService.setProfile(profile);
    }

    void drawRoute(Location start, Location end) {
        mobileStartPointToDraw = null;
        mobileEndPointToDraw = null;
        UPSRoute upsRoute = upsRouteService.getRoute(start, end);

        if (upsRoute != null) {
            steps = upsRoute.getSteps();
            coordinates = upsRoute.getCoordinates();
            distance = upsRoute.getDistance();
            duration = upsRoute.getDuration();
        }
        else
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver un itinéraire avec ces paramètres de navigation.",
                    "Impossible de trouver le chemin",
                    JOptionPane.ERROR_MESSAGE);
    }

    void drawRoute(GPSPoint start, GPSPoint end) {
        UPSRoute upsRoute = upsRouteService.getRoute(start, end);

        if (upsRoute != null) {
            steps = upsRoute.getSteps();
            coordinates = upsRoute.getCoordinates();
            distance = upsRoute.getDistance();
            duration = upsRoute.getDuration();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        try {
            BufferedImage image = ImageIO.read(new File("img/ups-map-real-sideways.png"));
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

        if (building != null) {
            g.drawString(building, (int) mouseCoordinate.getX(), (int) mouseCoordinate.getY());
        }
        g2.setStroke(new BasicStroke(2));

        g.setColor(Color.BLUE);
        g.drawOval((int)coordinates.get(0).gps2vue().getX() - 2, (int)coordinates.get(0).gps2vue().getY() - 2, 4, 4);
        //g.drawString(start.toString(), (int)coordinates.get(0).gps2vue().getX() - 2, (int)coordinates.get(0).gps2vue().getY() - 2);

        for (int i = 0; i < coordinates.size() - 1; i++) {
            if (wayPointsToDraw.contains(i))
                g2.setColor(Color.ORANGE);
            else
                g2.setColor(Color.RED);

            g2.drawLine((int)coordinates.get(i).gps2vue().getX(), (int)coordinates.get(i).gps2vue().getY(), (int)coordinates.get(i + 1).gps2vue().getX(), (int)coordinates.get(i + 1).gps2vue().getY());
        }

        g.setColor(Color.BLUE);
        g.drawOval((int)coordinates.get(coordinates.size() - 1).gps2vue().getX() - 2, (int)coordinates.get(coordinates.size() - 1).gps2vue().getY() - 2, 4, 4);
        //g.drawString(end.toString(), (int)coordinates.get(coordinates.size() - 1).gps2vue().getX() - 2, (int)coordinates.get(coordinates.size() - 1).gps2vue().getY() - 2);

        if (mobileStartPointToDraw != null) {
            g.setColor(Color.GREEN);
            Vecteur2D vecteur2D = new Vecteur2D(mobileStartPointToDraw.getLongitude(), mobileStartPointToDraw.getLatitude(),
                    gpsDownLeft,
                    gpsDownRight,
                    gpsUpLeft);
            g.fillOval((int)vecteur2D.gps2vue().getX(), (int)vecteur2D.gps2vue().getY(), 10, 10);
        }

        if (mobileEndPointToDraw != null) {
            g.setColor(Color.RED);
            Vecteur2D vecteur2D = new Vecteur2D(mobileEndPointToDraw.getLongitude(), mobileEndPointToDraw.getLatitude(),
                    gpsDownLeft,
                    gpsDownRight,
                    gpsUpLeft);
            g.fillOval((int)vecteur2D.gps2vue().getX(), (int)vecteur2D.gps2vue().getY(), 10, 10);
        }
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

    private void loadGpsConfig() {
        try {
            FileInputStream fileInputStream = new FileInputStream("data/gpsUpLeft.ser");
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            gpsUpLeft = (Coordinate)in.readObject();

            fileInputStream = new FileInputStream("data/gpsDownLeft.ser");
            in = new ObjectInputStream(fileInputStream);
            gpsDownLeft = (Coordinate)in.readObject();

            fileInputStream = new FileInputStream("data/gpsDownRight.ser");
            in = new ObjectInputStream(fileInputStream);
            gpsDownRight = (Coordinate)in.readObject();

            in.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof InvalidClassException) {
                gpsUpLeft = new Coordinate(1.46125, 43.5654);
                gpsDownLeft = new Coordinate(1.46127, 43.555555);
                gpsDownRight = new Coordinate(1.4749, 43.5556);
            }
            e.printStackTrace();
        }
    }

    public Coordinate getGpsDownLeft() {
        return gpsDownLeft;
    }

    public Coordinate getGpsDownRight() {
        return gpsDownRight;
    }

    public Coordinate getGpsUpLeft() {
        return gpsUpLeft;
    }

    Path getSteps() {
        return steps;
    }

    int getDuration() { return duration; }

    int getDistance(){ return distance;}

    void setMobileStartPointToDraw(GPSPoint gpsPoint) {
        if(mobileStartPointToDraw != null) {
            Boolean isDif = (gpsPoint.getLongitude() != mobileStartPointToDraw.getLongitude()) || (gpsPoint.getLatitude() != mobileStartPointToDraw.getLatitude());
            if (isDif) {
                mobileStartPointToDraw = gpsPoint;
                if (mobileEndPointToDraw != null) {
                    drawRoute(mobileStartPointToDraw, mobileEndPointToDraw);
                }
            }
        } else{
            mobileStartPointToDraw = gpsPoint;
        }
    }

    void setMobileEndPointToDraw(GPSPoint gpsPoint) {
        if(mobileEndPointToDraw != null) {
            Boolean isDif = (gpsPoint.getLongitude() != mobileEndPointToDraw.getLongitude()) || (gpsPoint.getLatitude() != mobileEndPointToDraw.getLatitude());
            if (isDif) {
                mobileEndPointToDraw = gpsPoint;
                if (mobileStartPointToDraw != null) {
                    drawRoute(mobileStartPointToDraw, mobileEndPointToDraw);
                }
            }
        } else{
            mobileEndPointToDraw = gpsPoint;
        }
    }

    void addAllWayPointsToDraw(List<Integer> points) {
        wayPointsToDraw.addAll(points);
    }

    void clearWayPointsToDraw() {
        wayPointsToDraw.clear();
    }

    class CalibrationWindow extends JFrame {

        CalibrationWindow() {
            super();
            this.setTitle("Calibrage");
            this.setSize(240, 310);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

            this.add(createConfig("Haut Gauche", gpsUpLeft));
            this.add(createConfig("Bas Gauche", gpsDownLeft));
            this.add(createConfig("Bas Droit", gpsDownRight));

            JButton cancelButton = new JButton("Annuler");
            cancelButton.addActionListener(e -> {
                loadGpsConfig();
                System.out.println(gpsUpLeft.getY());
            });

            JButton saveButton = new JButton("Sauvegarder");
            saveButton.addActionListener(e -> {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream("data/gpsUpLeft.ser");
                    ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
                    out.writeObject(gpsUpLeft);

                    fileOutputStream = new FileOutputStream("data/gpsDownLeft.ser");
                    out = new ObjectOutputStream(fileOutputStream);
                    out.writeObject(gpsDownLeft);

                    fileOutputStream = new FileOutputStream("data/gpsDownRight.ser");
                    out = new ObjectOutputStream(fileOutputStream);
                    out.writeObject(gpsDownRight);

                    fileOutputStream.close();
                    out.close();

                    JOptionPane.showMessageDialog(this,
                            "Les paramètres de calibration ont bien étaient sauvegardés.",
                            "Information",
                            JOptionPane.PLAIN_MESSAGE);
                } catch(IOException i) {
                    i.printStackTrace();
                }
            });

            JPanel buttonsPanel = new JPanel();
            //buttonsPanel.add(cancelButton); TODO : réparer ce bouton qui ne fonctionne pas
            buttonsPanel.add(saveButton);
            this.add(buttonsPanel);
        }

        private JPanel createConfig(String title, Coordinate coordinate) {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.PAGE_AXIS));
            jPanel.add(new JLabel(title));

            JLabel longitudeLabel = new JLabel("Longitude : " + coordinate.getX());
            longitudeLabel.setPreferredSize(new Dimension(120, 20));
            JLabel latitudeLabel = new JLabel("Latitude : " + coordinate.getY());
            latitudeLabel.setPreferredSize(new Dimension(120, 20));

            DecimalFormat df = new DecimalFormat("#.#####");
            df.setRoundingMode(RoundingMode.HALF_UP);

            ImageIcon plusIcon = new ImageIcon("img/plus-btn.png");
            ImageIcon minusIcon = new ImageIcon("img/minus-btn.png");

            JButton longitudeMinusButton = new JButton(minusIcon);
            longitudeMinusButton.setPreferredSize(new Dimension(20, 20));
            longitudeMinusButton.addActionListener(e -> {
                coordinate.setX(coordinate.getX() - 0.00001);
                longitudeLabel.setText("Longitude : " + df.format(coordinate.getX()));
            });

            JButton longitudePlusButton = new JButton(plusIcon);
            longitudePlusButton.setPreferredSize(new Dimension(20, 20));
            longitudePlusButton.addActionListener(e -> {
                coordinate.setX(coordinate.getX() + 0.00001);
                longitudeLabel.setText("Longitude : " + df.format(coordinate.getX()));
            });

            JButton latitudeMinusButton = new JButton(minusIcon);
            latitudeMinusButton.setPreferredSize(new Dimension(20, 20));
            latitudeMinusButton.addActionListener(e -> {
                coordinate.setY(coordinate.getY() - 0.00001);
                latitudeLabel.setText("Latitude : " + df.format(coordinate.getY()));
            });

            JButton latitudePlusButton = new JButton(plusIcon);
            latitudePlusButton.setPreferredSize(new Dimension(20, 20));
            latitudePlusButton.addActionListener(e -> {
                coordinate.setY(coordinate.getY() + 0.00001);
                latitudeLabel.setText("Latitude : " + df.format(coordinate.getY()));
            });

            JPanel subPanel = new JPanel();
            subPanel.add(longitudeLabel);
            subPanel.add(longitudeMinusButton);
            subPanel.add(longitudePlusButton);
            jPanel.add(subPanel);

            subPanel.add(latitudeLabel);
            subPanel.add(latitudeMinusButton);
            subPanel.add(latitudePlusButton);
            jPanel.add(subPanel);

            return jPanel;
        }

    }

    //Methods that have to be implemented with this interface
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}

