package Interface;

import Application.TextToSpeech;
import UPSRouteService.Instruction;
import UPSRouteService.Location;
import UPSRouteService.Path;
import UPSRouteService.Profile;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Window allowing the user to choose on screen his point of departure and arrival
 * it will also display the path information and instructions
 */

class NavigationWindow extends JFrame {

    private UPSMapPanel upsMapPanel;
    private DefaultListModel<Instruction> listSelectionModel = new DefaultListModel<>();
    private JLabel distanceLabel;
    private JLabel durationLabel;
    private JList<Instruction> jList;

    NavigationWindow(UPSMapPanel upsMapPanel) {
        super();
        this.setTitle("Navigation UPS");
        this.setSize(600, 240);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);

        this.upsMapPanel = upsMapPanel;

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.PAGE_AXIS));

        this.setLocation(0,240);

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

        durationLabel = new JLabel("Durée : " + upsMapPanel.getDuration()+" minute(s)");
        durationLabel.setPreferredSize(new Dimension(150, 20));
        durationPanel.add(durationLabel);

        distanceLabel = new JLabel("Distance : " + upsMapPanel.getDistance()+" metres");
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
            if (instruction != null && e.getValueIsAdjusting()) {
                upsMapPanel.clearWayPointsToDraw();

                // Retourne les points sous la forme [premier_point, dernier_point]
                List<Integer> list = instruction.getWayPoints();

                // Ajout de tous les autres points dans la liste entre les deux bornes
                for (int i = list.get(0) + 1; i < list.get(1); i++) {
                    if (!list.contains(i))
                        list.add(i);
                }

                TextToSpeech tts = new TextToSpeech();
                tts.setVoice("upmc-pierre-hsmm");
                tts.speak(instruction.toString(), 2.0f, false, true);

                upsMapPanel.addAllWayPointsToDraw(instruction.getWayPoints());
            }
        });

        JScrollPane jScrollPane = new JScrollPane(jList);
        this.add(jScrollPane);
        this.setVisible(true);
    }

    private void refreshJList() {
        durationLabel.setText("Durée : " + upsMapPanel.getDuration()+" minute(s)");
        distanceLabel.setText("Distance : " + upsMapPanel.getDistance()+" metres");
        Path steps = upsMapPanel.getSteps();
        listSelectionModel.removeAllElements();
        steps.forEach(i -> listSelectionModel.addElement(i));
        jList.setModel(listSelectionModel);
    }

}
