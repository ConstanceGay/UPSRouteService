package Application;

import Interface.CameraWindow;
import Interface.MapWindow;
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

    public static void main(String[] args) {
        new Thread(() -> {
            mapWindow = new MapWindow();
            mapWindow.open();
        }).start();

        new CameraWindow().runCamera();
    }

    public static MapWindow getMapWindow() {
        return mapWindow;
    }

}