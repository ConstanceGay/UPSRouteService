package Interface;

import javax.swing.*;

public class CameraWindow extends JFrame {

    private static UPSCameraPanel upsCameraPanel =null;

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

    public void stopCamera(){
        if (upsCameraPanel!=null) {
            upsCameraPanel.stopCamera();
        }
    }

    public static UPSCameraPanel getUpsCameraPanel() {
        return upsCameraPanel;
    }

}
