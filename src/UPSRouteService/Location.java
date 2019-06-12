package UPSRouteService;

public enum Location {

    GRAND_RU("1.463478,43.562038"),
    PETIT_RU("1.471717,43.560787"),
    BATIMENT_U3("1.467919,43.561805");

    private String coordinates;

    Location(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoordinates() {
        return coordinates;
    }
}
