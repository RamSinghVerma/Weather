package com.example.keshav.weather;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import Util.Utils;
import data.WeatherDataParser;
import data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener,View.OnKeyListener{
private String TAG="Weather Info ";
Weather weather=new Weather();
private Button button;
private TextView textView,textView2,textView3,sunset,sunrise,humidity,pressure,deg;
private EditText editText;
private ImageView imageView;
private ImageButton imageButton;
private String icon;
private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
         textView=findViewById(R.id.textView);
         textView2=findViewById(R.id.minTemp);
         textView3=findViewById(R.id.maxTemp);
         editText=findViewById(R.id.editText);
        sunset=findViewById(R.id.sunset);
        sunrise=findViewById(R.id.sunrise);
        humidity=findViewById(R.id.humidity);
        pressure=findViewById(R.id.pressure);
        imageView=findViewById(R.id.imageView);
        imageButton=findViewById(R.id.imageButton);
        button.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        imageButton.setOnLongClickListener(this);
       /* try {
            String weatherInfo=new WeatherInfo().execute("http://api.openweathermap.org/data/2.5/weather?q=Lucknow&APPID=b45b38a0c2142b2858f15c3fd91ed868").get();
            Log.i(TAG , weatherInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        renderAction();
    }

    public void renderAction(){
        prefs=getPreferences(MODE_PRIVATE);
        new WeatherInfo().execute(new String[]{prefs.getString("city","Lucknow")});
        new DownloadImage().execute();
    }

    @Override
    public void onClick(View v) {
        if (!editText.getText().toString().isEmpty()) {
            switch (v.getId()){
                case R.id.button:
                    prefs.edit().putString("city",editText.getText().toString()).commit();
                    new WeatherInfo().execute(new String[]{editText.getText().toString()});
                    new DownloadImage().execute();
                    break;
                case R.id.imageButton:
                    String text= editText.getText().toString();
                    editText.setText(text.substring(0,text.length()-1));
                    break;
            }
        }
        else {
            editText.setText("");
            textView.setText("");
            textView2.setText("");
            textView3.setText("");
            sunset.setText("");
            sunrise.setText("");
            humidity.setText("");
            pressure.setText("");
            imageView.setImageBitmap(null);
        }
    }

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()){
            case R.id.imageButton:
               editText.setText("");
               textView.setText("");
                textView2.setText("");
                textView3.setText("");
                sunset.setText("");
                sunrise.setText("");
                humidity.setText("");
                pressure.setText("");
                imageView.setImageBitmap(null);
              //  textView,textView2,textView3,sunset,sunrise,humidity,pressure
               return true;
        }
        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_ENTER){
            new WeatherInfo().execute(new String[]{editText.getText().toString()});
            new DownloadImage().execute();
            return true;
        }
        return false;
    }

    class WeatherInfo extends AsyncTask<String,Void,Weather>{

        @Override
        protected Weather doInBackground(String... strings) {
            String data=new WeatherHttpClient().getWeatherData(strings[0]);

            weather = WeatherDataParser.getWeather(data);

            return weather ;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            DateFormat df= DateFormat.getDateInstance();
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            textView.setText(weather.place.getCountry());
            editText.setText(weather.place.getCity());
            editText.setSelection(editText.getText().toString().length());
            textView2.setText(weather.temperature.getMinTemp()+"");
            textView3.setText(weather.temperature.getMaxTemp()+"");
            sunset.setText(formatter.format(new Date(weather.place.getSunset()).getTime())+"");
            sunrise.setText(formatter.format(new Date(weather.place.getSunrise()).getTime())+"");
            pressure.setText(weather.temperature.getPressure()+"");
            humidity.setText(weather.temperature.getHumidity()+"");


        }
    }

    class DownloadImage extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL url=new URL(Utils.ICON_URL + weather.getIcon() + ".png");
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                Bitmap bitmap=null;
                if (connection.getResponseCode()==HttpURLConnection.HTTP_OK)
                 bitmap= BitmapFactory.decodeStream(connection.getInputStream());

                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            imageView.setImageBitmap(bitmap);
        }
    }
}
