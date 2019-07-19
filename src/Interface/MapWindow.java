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
        this.setSize(940, 655);
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
                        frame.setSize(940, 655);
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

        this.setVisible(true);
    }

    public UPSMapPanel getMapPanel() {
        return upsMapPanel;
    }



}
