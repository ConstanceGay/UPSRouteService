package Interface;

import javax.swing.*;

public class CameraWindow extends JFrame {

    private static UPSCameraPanel upsCameraPanel;

    public CameraWindow() {
        super();
        this.setTitle("Vue Caméra");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void runCamera() {
        upsCameraPanel = new UPSCameraPanel();
        this.add(upsCameraPanel);
        this.pack();
        this.setVisible(true);
    }

    public static UPSCameraPanel getUpsCameraPanel() {
        return upsCameraPanel;
    }

}
