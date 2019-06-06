package UPSRouteService;

public enum Profile {

    WHEELCHAIR("wheelchair"), WALKING("foot-walking"), CYCLING("cycling-regular");

    private String apiProfileName;

    Profile(String apiProfileName) {
        this.apiProfileName = apiProfileName;
    }

    public String getAPIProfileName() {
        return apiProfileName;
    }

}
