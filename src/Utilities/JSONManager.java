package Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONManager {

    public static JSONObject getJSONObject(JSONObject jsonObject, String key) throws JSONException {
        String jsonValue = jsonObject.get(key).toString();

        if (jsonValue.charAt(0) == '[')
            jsonValue = jsonValue.substring(1);

        if (jsonValue.charAt(jsonValue.length() - 1) == ']')
            jsonValue = jsonValue.substring(0, jsonValue.length() - 1);

        return new JSONObject(jsonValue);
    }

    public static JSONArray getJSONArray(JSONObject jsonObject, String key) throws JSONException {
        JSONArray jsonArray;

        String stepsStr = jsonObject.get(key).toString();
        jsonArray = new JSONArray(stepsStr);

        return jsonArray;
    }

}
