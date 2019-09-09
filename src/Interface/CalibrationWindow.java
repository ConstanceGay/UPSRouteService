package Interface;

import Application.Main;
import UPSRouteService.GPSPoint;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Window allowing the user to adjust the coordinates of the corner of the map
 */

class CalibrationWindow extends JDialog {

    CalibrationWindow() {
        super();
        this.setTitle("Calibrage");
        this.setSize(240, 310);
        this.setResizable(false);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        this.add(createConfig("Haut Gauche", Main.getMapWindow().getMapPanel().getGpsUpLeft()));
        this.add(createConfig("Bas Gauche", Main.getMapWindow().getMapPanel().getGpsDownLeft()));
        this.add(createConfig("Bas Droit", Main.getMapWindow().getMapPanel().getGpsDownRight()));

        JButton saveButton = new JButton("Sauvegarder");
        saveButton.addActionListener(e -> {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("res/gpsUpLeft.ser");

                ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
                out.writeObject(Main.getMapWindow().getMapPanel().getGpsUpLeft());

                fileOutputStream = new FileOutputStream("res/gpsDownLeft.ser");

                out = new ObjectOutputStream(fileOutputStream);
                out.writeObject(Main.getMapWindow().getMapPanel().getGpsDownLeft());

                fileOutputStream = new FileOutputStream("res/gpsDownRight.ser");

                out = new ObjectOutputStream(fileOutputStream);
                out.writeObject(Main.getMapWindow().getMapPanel().getGpsDownRight());

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
        buttonsPanel.add(saveButton);
        this.add(buttonsPanel);
    }

    private JPanel createConfig(String title, GPSPoint coordinate) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.PAGE_AXIS));
        jPanel.add(new JLabel(title));

        JLabel longitudeLabel = new JLabel("Longitude : " + coordinate.getLongitude());
        longitudeLabel.setPreferredSize(new Dimension(120, 20));
        JLabel latitudeLabel = new JLabel("Latitude : " + coordinate.getLatitude());
        latitudeLabel.setPreferredSize(new Dimension(120, 20));

        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.HALF_UP);

        ImageIcon plusIcon = new ImageIcon(getClass().getResource("/plus-btn.png"));
        ImageIcon minusIcon = new ImageIcon(getClass().getResource("/minus-btn.png"));

        JButton longitudeMinusButton = new JButton(minusIcon);
        longitudeMinusButton.setPreferredSize(new Dimension(20, 20));
        longitudeMinusButton.addActionListener(e -> {
            coordinate.setLongitude(coordinate.getLongitude() - 0.00001);
            longitudeLabel.setText("Longitude : " + df.format(coordinate.getLongitude()));
        });

        JButton longitudePlusButton = new JButton(plusIcon);
        longitudePlusButton.setPreferredSize(new Dimension(20, 20));
        longitudePlusButton.addActionListener(e -> {
            coordinate.setLongitude(coordinate.getLongitude() + 0.00001);
            longitudeLabel.setText("Longitude : " + df.format(coordinate.getLongitude()));
        });

        JButton latitudeMinusButton = new JButton(minusIcon);
        latitudeMinusButton.setPreferredSize(new Dimension(20, 20));
        latitudeMinusButton.addActionListener(e -> {
            coordinate.setLatitude(coordinate.getLatitude() - 0.00001);
            latitudeLabel.setText("Latitude : " + df.format(coordinate.getLatitude()));
        });

        JButton latitudePlusButton = new JButton(plusIcon);
        latitudePlusButton.setPreferredSize(new Dimension(20, 20));
        latitudePlusButton.addActionListener(e -> {
            coordinate.setLatitude(coordinate.getLatitude() + 0.00001);
            latitudeLabel.setText("Latitude : " + df.format(coordinate.getLatitude()));
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
