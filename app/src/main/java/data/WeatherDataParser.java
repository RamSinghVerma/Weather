package data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Locale;
import Util.Utils;
import model.Place;
import model.Temperature;
import model.Weather;

public class WeatherDataParser {

    public static Weather getWeather(String data) {
        Weather weather = new Weather();
        try {
            JSONObject jsonObject =new JSONObject(data);
            // Setting co-ordinate data
            Place place=new Place();
            JSONObject coordObj=Utils.getObject("coord",jsonObject);
            place.setLon(Utils.getFloat("lon",coordObj));
            place.setLat(Utils.getFloat("lat",coordObj));
            // Setting Other data
            JSONObject sysObject=Utils.getObject("sys",jsonObject);

            Locale locale=new Locale("en",Utils.getString("country",sysObject));

            place.setCountry(locale.getDisplayCountry());
            place.setCity(Utils.getString("name",jsonObject));
            place.setSunset(Utils.getLong("sunset",sysObject));
            place.setSunrise(Utils.getLong("sunrise",sysObject));
            place.setLastUpdated(Utils.getLong("dt",jsonObject));
            weather.place=place;

            // setting temperature
            JSONObject main=Utils.getObject("main",jsonObject);
            Temperature temperature=new Temperature();
            temperature.setTemp(Utils.getFloat("temp",main));
            temperature.setMinTemp(Utils.getFloat("temp_min",main));
            temperature.setMaxTemp(Utils.getFloat("temp_max",main));
            temperature.setHumidity(Utils.getFloat("humidity",main));
            temperature.setPressure(Utils.getFloat("pressure",main));
            weather.temperature=temperature;

            JSONArray jsonArray=jsonObject.getJSONArray("weather");
            JSONObject jsonObject1=jsonArray.getJSONObject(0);
            weather.setIcon(Utils.getString("icon",jsonObject1));

            return weather;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
