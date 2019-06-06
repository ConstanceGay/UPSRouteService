package UPSRouteService;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UPSRouteService {

    private static final String API_KEY = "5b3ce3597851110001cf6248e17e16eae3fd4c47a7f3738528afba68";
    private Profile profile = Profile.WALKING;

    public UPSRouteService() {

    }

    public UPSRoute getRoute(Location start, Location end) {
        JSONObject jsonObject = null;

        try {
            URL url = new URL("https://api.openrouteservice.org/v2/directions/" + profile.getAPIProfileName() + "?api_key=" + API_KEY + "&start=" + start.getCoordinates() + "&end=" + end.getCoordinates());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");


            String content = getStream(connection.getInputStream());
            connection.disconnect();

            if (content != null)
                jsonObject = new JSONObject(content);
            else
                return null;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            return new UPSRoute(jsonObject, false);
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

    private String getStream(InputStream inputStream) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            StringBuffer content = new StringBuffer();

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

}
