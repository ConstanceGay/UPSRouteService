package Interface;

import javax.swing.*;

public class CameraWindow extends JFrame {

    public CameraWindow() {
        super();
        this.setTitle("Vue Caméra");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void runCamera() {
        this.add(new UPSCameraPanel());
        this.pack();
        this.setVisible(true);
    }

}
