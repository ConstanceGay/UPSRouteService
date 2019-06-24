package Application;

import Interface.MapWindow;
import UPSRouteService.Location;
import UPSRouteService.UPSRouteService;

public class Main {

    private static MapWindow mapWindow;

    public static void main(String[] args) {
        mapWindow = new MapWindow();
        mapWindow.open();

        /*UPSRouteService upsRouteService = new UPSRouteService();
        System.out.println(" - Request HTTP GET - ");
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU));
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU).getSteps());
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU).getCoordinates());
        System.out.println(upsRouteService.getBuilding(1.469443,43.561269));

        System.out.println("\n - Request HTTP POST - ");
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU, "fr"));
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU, "fr").getSteps());
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU, "fr").getCoordinates());*/
    }

    public static MapWindow getMapWindow() {
        return mapWindow;
    }

}