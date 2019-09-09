package Interface;

import UPSRouteService.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Window displaying the map
 */

public class MapWindow extends JFrame {

    private UPSMapPanel upsMapPanel;
    private boolean isFullScreen = false;

    public MapWindow() {
        super();
        this.setTitle("Carte de l'UPS");
        this.setSize(940, 655);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setFocusable(true);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - this.getWidth();
        int y = (int) rect.getMaxY() - this.getHeight();
        this.setLocation(x,y);

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
