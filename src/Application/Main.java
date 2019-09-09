package Application;

import Interface.*;

public class Main {

    private static MapWindow mapWindow;

    public static void main(String[] args) {

        //Opens the window containing the Map
        mapWindow = new MapWindow();
        mapWindow.open();

        //Opens the mode window
        ModeWindow modeWindow  = new ModeWindow();
        modeWindow.setVisible(true);

    }

    public static MapWindow getMapWindow() {
        return mapWindow;
    }

}