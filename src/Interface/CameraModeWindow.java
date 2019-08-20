package Interface;

import Application.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CameraModeWindow extends JFrame{

    public CameraModeWindow() {

        this.setTitle("Mode d'utilisation de la maquette");
        this.setSize(600, 240);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

        JPanel CameraModePanel = new JPanel();
        CameraModePanel.setLayout(new BoxLayout(CameraModePanel, BoxLayout.PAGE_AXIS));

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

        JRadioButton navigationButton   = new JRadioButton("Navigation");
        JRadioButton explorationButton  = new JRadioButton("Exploration");
        JToggleButton confirmButton = new JToggleButton("Valider le parcours");

        navigationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });

        explorationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = confirmButton.getModel().isSelected();
                if (!selected){
                    Main.getMapWindow().getMapPanel().resetRouteConfirm();
                } else if (!Main.getMapWindow().getMapPanel().setRouteConfirm()){
                    confirmButton.getModel().setSelected(false);
                }
            }
        });

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(4, 1));
        radioPanel.add(navigationButton);
        radioPanel.add(explorationButton);
        radioPanel.add(confirmButton);

        radioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Quel mode de camera souhaitez-vous?"));
        setContentPane(radioPanel);  // Button panel is only content.
        pack();
    }
}
