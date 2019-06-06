package UPSRouteService;

import Utilities.JSONManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class UPSRoute {

    private JSONObject json;
    private boolean postCall;

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
                    coordinatesList.add(new Vecteur2D(Double.parseDouble(coordinate.split(",")[0]), Double.parseDouble(coordinate.split(",")[1])));
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
            return getPath(steps);
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
            return getPath(steps);
        } else {
            return null;
        }
    }

    private Path getPath(JSONArray steps) {
        Path path = new Path();

        for (int i = 0; i < steps.length(); i++) {
            try {
                String jsonStep = steps.get(i).toString();
                JSONObject step = new JSONObject(jsonStep);

                String name = step.get("name").toString();
                int type = Integer.parseInt(step.get("type").toString());
                String wayPointsStr = step.get("way_points").toString().replace("[", "").replace("]", "");
                List<String> temp = new ArrayList<>(Arrays.asList(wayPointsStr.split(",")));
                List<Integer> wayPoints = new ArrayList<>();
                for (String str : temp) {
                    wayPoints.add(Integer.parseInt(str));
                }

                path.add(new Instruction(name, type, wayPoints));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return path;
    }

    public String toString() {
        return json.toString();
    }

}
