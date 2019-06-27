package Interface;

import UPSRouteService.Location;
import UPSRouteService.Profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MapWindow extends JFrame {

    private UPSMapPanel upsMapPanel;
    private boolean isFullScreen = false;

    public MapWindow() {
        super();
        this.setTitle("Maquette UPS");
        this.setSize(1090, 634);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setFocusable(true);

        MapWindow frame = this;
        this.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    if (isFullScreen) {
                        frame.dispose();
                        frame.setExtendedState(JFrame.NORMAL);
                        frame.setUndecorated(false);
                        frame.setSize(702, 682);
                        frame.setVisible(true);
                        isFullScreen = false;
                    } else {
                        frame.dispose();
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        frame.setUndecorated(true);
                        frame.setVisible(true);
                        isFullScreen = true;
                    }

                    frame.repaint();
                    frame.setVisible(true);
                }
            }
            public void keyPressed(KeyEvent e) {

            }
        });
    }

    public void open() {
        upsMapPanel = new UPSMapPanel(Location.BATIMENT_U3, Location.IRIT);
        upsMapPanel.setFocusable(true);
        this.add(upsMapPanel);

        new NavigationWindow();

        this.setVisible(true);
    }

    public UPSMapPanel getMapPanel() {
        return upsMapPanel;
    }

    class NavigationWindow extends JFrame {

        NavigationWindow() {
            super();
            this.setTitle("Navigation UPS");
            this.setSize(280, 174);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

            JPanel profilePanel = new JPanel();
            JPanel startPanel = new JPanel();
            JPanel endPanel = new JPanel();

            JLabel profileLabel = new JLabel("Profil");
            profileLabel.setPreferredSize(new Dimension(50, 20));
            profilePanel.add(profileLabel);

            JComboBox<Profile> profileCombo = new JComboBox<>(Profile.values());
            profileCombo.setSelectedIndex(1);
            profilePanel.add(profileCombo);

            JLabel startLabel = new JLabel("Départ");
            startLabel.setPreferredSize(new Dimension(50, 20));
            startPanel.add(startLabel);

            JComboBox<Location> startCombo = new JComboBox<>(Location.values());
            startCombo.setSelectedIndex(0);
            startPanel.add(startCombo);

            JLabel endLabel = new JLabel("Arrivée");
            endLabel.setPreferredSize(new Dimension(50, 20));
            endPanel.add(endLabel);

            JComboBox<Location> endCombo = new JComboBox<>(Location.values());
            endCombo.setSelectedIndex(1);
            endPanel.add(endCombo);

            this.add(profilePanel);
            this.add(startPanel);
            this.add(endPanel);

            JButton jButton = new JButton("Valider");
            jButton.addActionListener(e -> {
                upsMapPanel.setProfile((Profile)profileCombo.getSelectedItem());
                upsMapPanel.drawRoute((Location)startCombo.getSelectedItem(), (Location)endCombo.getSelectedItem());
            });
            this.add(jButton);
            this.add(new JPanel());

            this.setVisible(true);
        }

    }

}
