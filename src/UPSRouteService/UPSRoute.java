package UPSRouteService;

import Interface.UPSMapPanel;
import Utilities.JSONManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class UPSRoute {

    private JSONObject json;
    private boolean postCall;
    private UPSMapPanel upsMapPanel;

    UPSRoute(JSONObject json, UPSMapPanel upsMapPanel, boolean postCall) {
        this.json = json;
        this.postCall = postCall;
        this.upsMapPanel = upsMapPanel;
    }

    UPSRoute(JSONObject json, boolean postCall) {
        this.json = json;
        this.postCall = postCall;
    }

    public Path getSteps() {
        if (postCall) {
            return getStepsFromPOST();
        } else {
            return getStepsFromGET();
        }
    }

    //Returns the length of a path in meters
    public int getDistance(){
        JSONObject jsonObject = json;
        int int_dist;
        try {
            jsonObject = JSONManager.getJSONObject(jsonObject, "features");
            jsonObject = JSONManager.getJSONObject(jsonObject, "properties");
            jsonObject = JSONManager.getJSONObject(jsonObject, "summary");
            int_dist = jsonObject.getInt("distance");
            return int_dist;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //Returns the duration of path in minutes
    public int getDuration(){
        JSONObject jsonObject = json;
        int int_duration;
        try {
            jsonObject = JSONManager.getJSONObject(jsonObject, "features");
            jsonObject = JSONManager.getJSONObject(jsonObject, "properties");
            jsonObject = JSONManager.getJSONObject(jsonObject, "summary");
            int_duration = jsonObject.getInt("duration");
            return int_duration/60;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Vecteur2D> getCoordinates() {
        if (postCall) {
            // TODO : récupérer les coordonnées depuis une requête HTTP GET
            System.out.println("Impossible de lire les coordonnées depuis le JSON.");
            return null;
        }

        JSONObject jsonObject = json;
        JSONArray coordinates = null;

        try {
            jsonObject = JSONManager.getJSONObject(jsonObject, "features");
            jsonObject = JSONManager.getJSONObject(jsonObject, "geometry");
            coordinates = JSONManager.getJSONArray(jsonObject, "coordinates");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (coordinates != null) {
            List<Vecteur2D> coordinatesList = new ArrayList<>();

            for (int i = 0; i < coordinates.length(); i++) {
                try {
                    String coordinate = coordinates.get(i).toString();
                    coordinate = coordinate.replace("[", "");
                    coordinate = coordinate.replace("]", "");

                    if (upsMapPanel == null) {
                        coordinatesList.add(new Vecteur2D(Double.parseDouble(coordinate.split(",")[0]),
                                Double.parseDouble(coordinate.split(",")[1])));
                    } else {
                        coordinatesList.add(new Vecteur2D(Double.parseDouble(coordinate.split(",")[0]),
                                Double.parseDouble(coordinate.split(",")[1]),
                                upsMapPanel.getGpsDownLeft(),
                                upsMapPanel.getGpsDownRight(),
                                upsMapPanel.getGpsUpLeft()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return coordinatesList;
        } else {
            return null;
        }
    }

    private Path getStepsFromGET() {
        JSONObject jsonObject = json;
        JSONArray steps = null;

        try {
            jsonObject = JSONManager.getJSONObject(jsonObject, "features");
            jsonObject = JSONManager.getJSONObject(jsonObject, "properties");
            jsonObject = JSONManager.getJSONObject(jsonObject, "segments");
            steps = JSONManager.getJSONArray(jsonObject, "steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (steps != null) {
            Path path = new Path();

            for (int i = 0; i < steps.length(); i++) {
                try {
                    String jsonStep = steps.get(i).toString();
                    JSONObject step = new JSONObject(jsonStep);

                    String name = step.get("name").toString();
                    int type = Integer.parseInt(step.get("type").toString());
                    String wayPointsStr = step.get("way_points").toString().replace("[", "").replace("]", "");
                    path.add(new Instruction(name, type, getWayPoints(wayPointsStr)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return path;
        } else {
            return null;
        }
    }

    private Path getStepsFromPOST() {
        JSONObject jsonObject = json;
        JSONArray steps = null;

        try {
            jsonObject = JSONManager.getJSONObject(jsonObject, "routes");
            jsonObject = JSONManager.getJSONObject(jsonObject, "segments");
            steps = JSONManager.getJSONArray(jsonObject, "steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (steps != null) {
            Path path = new Path();

            for (int i = 0; i < steps.length(); i++) {
                try {
                    String jsonStep = steps.get(i).toString();
                    JSONObject step = new JSONObject(jsonStep);

                    String instruction = step.get("instruction").toString();
                    String wayPointsStr = step.get("way_points").toString().replace("[", "").replace("]", "");
                    path.add(new Instruction(getWayPoints(wayPointsStr), instruction));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return path;
        } else {
            return null;
        }
    }

    private List<Integer> getWayPoints(String wayPoints) {
        List<String> temp = new ArrayList<>(Arrays.asList(wayPoints.split(",")));
        List<Integer> wayPointsList = new ArrayList<>();
        for (String str : temp) {
            wayPointsList.add(Integer.parseInt(str));
        }

        return wayPointsList;
    }

    public String toString() {
        return json.toString();
    }

}
