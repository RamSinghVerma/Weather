package Util;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

    public static final String BASE_URL="http://api.openweathermap.org/data/2.5/weather?q=";
    public static final String ICON_URL="http://openweathermap.org/img/w/";
    public static final String SUFFIX_URL="&APPID=b45b38a0c2142b2858f15c3fd91ed868";

    public static JSONObject getObject(String tagName,JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject(tagName);
    }

    public static double getDouble(String tagName,JSONObject jsonObject) throws JSONException {
        return jsonObject.getDouble(tagName);
    }

    public static float getFloat(String tagName,JSONObject jsonObject) throws JSONException {
        return (float)jsonObject.getDouble(tagName);
    }

    public static int getInt(String tagName,JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt(tagName);
    }

    public static long getLong(String tagName,JSONObject jsonObject) throws JSONException {
        return jsonObject.getLong(tagName);
    }

    public static String getString(String tagName,JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tagName);
    }
}
