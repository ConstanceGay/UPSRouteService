package Interface;

import UPSRouteService.Instruction;
import UPSRouteService.Path;

import javax.swing.*;
import java.awt.*;

/**
 * Window giving the navigation instructions for a topcode path
 */

class InstructionWindow extends JFrame{

    private DefaultListModel<Instruction> listSelectionModel = new DefaultListModel<>();
    private JLabel distanceLabel;
    private JLabel durationLabel;
    private JLabel startBuildingLabel;
    private JLabel endBuildingLabel;
    private JList<Instruction> jList;

    InstructionWindow(int distance, int duration, Path steps) {
        //super();
        this.setTitle("Instruction UPS");
        this.setSize(600, 240);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

        JPanel InstructionPanel = new JPanel();
        InstructionPanel.setLayout(new BoxLayout(InstructionPanel, BoxLayout.PAGE_AXIS));

        JPanel startBuildingPanel = new JPanel();
        JPanel endBuildingPanel = new JPanel();
        JPanel durationPanel = new JPanel();
        JPanel distancePanel = new JPanel();

        startBuildingLabel = new JLabel(" ");
        startBuildingPanel.setPreferredSize(new Dimension(150, 20));
        startBuildingPanel.add(startBuildingLabel);

        endBuildingLabel = new JLabel(" ");
        endBuildingPanel.setPreferredSize(new Dimension(150, 20));
        endBuildingPanel.add(endBuildingLabel);

        durationLabel = new JLabel("Durée : " +duration+" minute(s)");
        durationLabel.setPreferredSize(new Dimension(150, 20));
        durationPanel.add(durationLabel);

        distanceLabel = new JLabel("Distance : " + distance+" metres");
        distanceLabel.setPreferredSize(new Dimension(150, 20));
        distancePanel.add(distanceLabel);

        InstructionPanel.add(startBuildingPanel);
        InstructionPanel.add(endBuildingPanel);
        InstructionPanel.add(durationPanel);
        InstructionPanel.add(distancePanel);
        InstructionPanel.add(new JPanel());

        this.add(InstructionPanel);

        listSelectionModel.removeAllElements();

        if (steps != null) {
            steps.forEach(i -> listSelectionModel.addElement(i));
        }

        jList = new JList<>();
        jList.setModel(listSelectionModel);
        JScrollPane jScrollPane = new JScrollPane(jList);
        this.add(jScrollPane);
    }

    void refresh(int distance, int duration, Path steps, String startBuilding, String endBuilding) {
        startBuildingLabel.setText("Départ : "+ startBuilding);
        endBuildingLabel.setText("Arrivée : "+endBuilding);
        durationLabel.setText("Durée : " + duration +" minute(s)");
        distanceLabel.setText("Distance : " + distance+" metres");

        listSelectionModel.removeAllElements();
        steps.forEach(i -> listSelectionModel.addElement(i));
        jList.setModel(listSelectionModel);
    }

}

