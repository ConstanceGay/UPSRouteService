package Interface;

import UPSRouteService.Location;

import javax.swing.*;

public class Window extends JFrame {

    public Window() {
        super();
        this.setTitle("Maquette UPS");
        this.setSize(1043, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void open() {
        UPSMapPanel UPSMapPanel = new UPSMapPanel(Location.GRAND_RU, Location.PETIT_RU);
        this.add(UPSMapPanel);

        this.setVisible(true);
    }

}
