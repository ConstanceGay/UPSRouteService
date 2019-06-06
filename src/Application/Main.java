package Application;

import UPSRouteService.Location;
import UPSRouteService.UPSRouteService;

public class Main {

    public static void main(String[] args) {
        UPSRouteService upsRouteService = new UPSRouteService();

        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU));
        //System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU).getSteps());
        System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU).getCoordinates());

        //System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU, "fr"));
        //System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU, "fr").getSteps());
        //System.out.println(upsRouteService.getRoute(Location.GRAND_RU, Location.PETIT_RU, "fr").getCoordinates());
    }

}