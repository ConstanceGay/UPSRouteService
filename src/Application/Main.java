package Application;

import Interface.*;
import UPSRouteService.Location;
import UPSRouteService.UPSRouteService;
import Utilities.Scanner;
import Utilities.TopCode;
import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    private static MapWindow mapWindow;
    private static ModeWindow modeWindow;


    public static void main(String[] args) {
        mapWindow = new MapWindow();
        mapWindow.open();

        modeWindow  = new ModeWindow();
        modeWindow.setVisible(true);

    }

    public static MapWindow getMapWindow() {
        return mapWindow;
    }

}