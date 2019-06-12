package Application;

import Interface.Window;

public class Main {

    private static Window window;

    public static void main(String[] args) {
        /*UPSRouteService upsRouteService = new UPSRouteService();

        System.out.println(" - Request HTTP GET - ");
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU));
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU).getSteps());
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU).getCoordinates());

        System.out.println("\n - Request HTTP POST - ");
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU, "fr"));
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU, "fr").getSteps());
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU, "fr").getCoordinates());*/

        window = new Window();
        window.open();
    }

    public static Window getWindow() {
        return window;
    }

}