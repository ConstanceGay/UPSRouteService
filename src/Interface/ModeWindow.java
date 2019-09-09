package Interface;

import Application.Main;
import javax.swing.*;
import java.awt.*;

/**
 * Window allowing the user to choose which mode he wants to use the app for : camera, navigation or calibration
 */

public class ModeWindow extends JFrame {


    private static CalibrationWindow calibrationWindow;
    private static NavigationWindow navigationWindow;
    private static CameraWindow cameraWindow;
    private static CameraModeWindow cameraModeWindow;

    public ModeWindow(){
        super();
        this.setTitle("Mode de Navigation");
        this.setSize(600, 240);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.PAGE_AXIS));

        calibrationWindow = new CalibrationWindow();
        calibrationWindow.setVisible(false);
        navigationWindow= new NavigationWindow(Main.getMapWindow().getMapPanel());
        navigationWindow.setVisible(false);
        cameraWindow = new CameraWindow();
        cameraWindow.setVisible(false);
        cameraModeWindow = new CameraModeWindow();
        cameraModeWindow.setVisible(false);

        JRadioButton navigationButton   = new JRadioButton("Navigation");
        JRadioButton calibrationButton    = new JRadioButton("Calibration");
        JRadioButton cameraButton    = new JRadioButton("Camera");
        JToggleButton imageButton = new JToggleButton("Image");

        calibrationButton.addActionListener(e ->  {
                cameraWindow.setVisible(false);
                cameraWindow.stopCamera();
                cameraModeWindow.setVisible(false);
                navigationWindow.setVisible(false);
                calibrationWindow.setVisible(true);
        });

        navigationButton.addActionListener(e -> {
                cameraWindow.setVisible(false);
                cameraWindow.stopCamera();
                cameraModeWindow.setVisible(false);
                navigationWindow.setVisible(true);
                calibrationWindow.setVisible(false);
        });

        cameraButton.addActionListener(e -> {
                cameraWindow.setVisible(true);
                cameraWindow.runCamera();
                cameraModeWindow.setVisible(true);
                navigationWindow.setVisible(false);
                calibrationWindow.setVisible(false);
        });

        imageButton.addActionListener(e -> {
                boolean selected = imageButton.getModel().isSelected();
                if (!selected){
                    Main.getMapWindow().getMapPanel().setImage("/ups-map-9x6.png");
                } else{
                    Main.getMapWindow().getMapPanel().setImage("/black_map.png");
                }
        });

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(4, 1));
        radioPanel.add(navigationButton);
        radioPanel.add(calibrationButton);
        radioPanel.add(cameraButton);
        radioPanel.add(imageButton);

        radioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Quel mode souhaitez-vous?"));
        setContentPane(radioPanel);  // Button panel is only content.
        pack();
    }

}
