package data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Util.Utils;
import model.Weather;

public class WeatherHttpClient {

    public String getWeatherData(String place){
        try{
            URL url=new URL(Utils.BASE_URL + place + Utils.SUFFIX_URL);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            InputStream inputStream=connection.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);

            int data=inputStreamReader.read();
            char currentData;
            StringBuilder weatherDetail=new StringBuilder();
            // reading data
            while (data!=-1){
                currentData=(char)data;
                weatherDetail.append(currentData);
                data=inputStreamReader.read();
            }
            // closing object
            inputStreamReader.close();
            inputStream.close();
            connection.disconnect();

            return weatherDetail.toString();
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
}
