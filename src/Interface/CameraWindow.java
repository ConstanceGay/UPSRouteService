package Interface;

import javax.swing.*;

/**
 * Window displaying the camera view
 */
class CameraWindow extends JFrame {

    private static UPSCameraPanel upsCameraPanel =null;

    CameraWindow() {
        super();
        this.setTitle("Vue Camera");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //Setting the camera window to visible
    void runCamera() {
        upsCameraPanel = new UPSCameraPanel();
        this.add(upsCameraPanel);
        this.pack();
        this.setVisible(true);
    }

    //Turning off the camera
    void stopCamera(){
        if (upsCameraPanel!=null) {
            upsCameraPanel.stopCamera();
        }
    }

}
