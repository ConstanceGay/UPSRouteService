package Interface;

import UPSRouteService.Instruction;
import UPSRouteService.Location;
import UPSRouteService.Path;
import UPSRouteService.Profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

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

        private DefaultListModel<Instruction> listSelectionModel = new DefaultListModel<>();
        private JList<Instruction> jList;

        NavigationWindow() {
            super();
            this.setTitle("Navigation UPS");
            this.setSize(440, 240);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(true);

            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

            JPanel navigationPanel = new JPanel();
            navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.PAGE_AXIS));

            JPanel profilePanel = new JPanel();
            JPanel startPanel = new JPanel();
            JPanel endPanel = new JPanel();
            JPanel durationPanel = new JPanel();
            JPanel distancePanel = new JPanel();

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

            navigationPanel.add(profilePanel);
            navigationPanel.add(startPanel);
            navigationPanel.add(endPanel);

            JButton jButton = new JButton("Valider");
            jButton.addActionListener(e -> {
                upsMapPanel.setProfile((Profile)profileCombo.getSelectedItem());
                upsMapPanel.drawRoute((Location)startCombo.getSelectedItem(), (Location)endCombo.getSelectedItem());
                refreshJList();
            });
            navigationPanel.add(jButton);

            // Espacement entre le bouton et le bas de la fenêtre
            JLabel durationLabel = new JLabel("Durée : " + upsMapPanel.getDuration()+" minute(s)");
            durationLabel.setPreferredSize(new Dimension(150, 20));
            durationPanel.add(durationLabel);

            JLabel distanceLabel = new JLabel("Distance : " + upsMapPanel.getDistance()+" metres");
            distanceLabel.setPreferredSize(new Dimension(150, 20));
            distancePanel.add(distanceLabel);

            navigationPanel.add(durationPanel);
            navigationPanel.add(distancePanel);
            navigationPanel.add(new JPanel());


            this.add(navigationPanel);

            Path steps = upsMapPanel.getSteps();
            listSelectionModel.removeAllElements();
            steps.forEach(i -> listSelectionModel.addElement(i));

            jList = new JList<>();
            jList.setModel(listSelectionModel);
            jList.addListSelectionListener(e -> {
                Instruction instruction = jList.getSelectedValue();
                if (instruction != null) {
                    upsMapPanel.clearWayPointsToDraw();

                    // Retourne les points sous la forme [premier_point, dernier_point]
                    List<Integer> list = instruction.getWayPoints();

                    // Ajout de tous les autres points dans la liste entre les deux bornes
                    for (int i = list.get(0) + 1; i < list.get(1); i++) {
                        if (!list.contains(i))
                            list.add(i);
                    }

                    upsMapPanel.addAllWayPointsToDraw(instruction.getWayPoints());
                }
            });

            JScrollPane jScrollPane = new JScrollPane(jList);
            this.add(jScrollPane);

            this.setVisible(true);
        }

        private void refreshJList() {
            Path steps = upsMapPanel.getSteps();
            listSelectionModel.removeAllElements();
            steps.forEach(i -> listSelectionModel.addElement(i));
            jList.setModel(listSelectionModel);
        }

    }

}
