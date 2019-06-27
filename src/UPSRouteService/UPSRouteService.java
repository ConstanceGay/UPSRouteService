package UPSRouteService;

import Interface.UPSMapPanel;
import Utilities.JSONManager;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UPSRouteService {

    private static final String API_KEY = "5b3ce3597851110001cf6248e17e16eae3fd4c47a7f3738528afba68";
    private static final int SUCCESSFUL_REQUEST = 200;
    private Profile profile = Profile.WALKING;
    private UPSMapPanel upsMapPanel;

    public UPSRouteService() {
    }

    public UPSRouteService(UPSMapPanel upsMapPanel) {
        this.upsMapPanel = upsMapPanel;
    }

    public UPSRoute getRoute(Location start, Location end) {
        JSONObject jsonObject = null;

        try {
            URL url = new URL("https://api.openrouteservice.org/v2/directions/" + profile.getAPIProfileName() + "?api_key=" + API_KEY + "&start=" + start.getCoordinates() + "&end=" + end.getCoordinates());
            jsonObject = sendGETRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            return new UPSRoute(jsonObject, upsMapPanel, false);
        } else {
            return null;
        }
    }

    public UPSRoute getRoute(Location start, Location end, String language) {
        if (language.equals("fr")) {
            JSONObject jsonObject = null;

            try {
                URL url = new URL("https://api.openrouteservice.org/v2/directions/" + profile.getAPIProfileName() + "/json");
                String payload = "{\"coordinates\":[[" + start.getCoordinates() + "],[" + end.getCoordinates() + "]],\"language\":\"fr\"}";
                byte[] postData = payload.getBytes(StandardCharsets.UTF_8);

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", API_KEY);
                connection.setRequestProperty("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.write(postData);
                }

                String content = getStream(connection.getInputStream());
                connection.disconnect();

                if (content != null)
                    jsonObject = new JSONObject(content);
                else
                    return null;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return new UPSRoute(jsonObject, true);
        } else {
            return null;
        }
    }

    public String getBuilding(double longitude, double latitude) {
        JSONObject jsonObject = null;

        try {
            URL url = new URL("https://api.openrouteservice.org/geocode/reverse?api_key="  + API_KEY + "&point.lon=" + longitude + "&point.lat=" + latitude + "&boundary.circle.radius=0.2&size=1&layers=venue&boundary.country=fr");
            jsonObject = sendGETRequest(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            try {
                jsonObject = JSONManager.getJSONObject(jsonObject, "features");
                jsonObject = JSONManager.getJSONObject(jsonObject, "properties");
                return jsonObject.get("name").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    private String getStream(InputStream inputStream) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            return content.toString();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private JSONObject sendGETRequest(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");

            if (connection.getResponseCode() == SUCCESSFUL_REQUEST) {
                String content = getStream(connection.getInputStream());
                connection.disconnect();

                if (content != null)
                    return new JSONObject(content);
                else
                    return null;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
