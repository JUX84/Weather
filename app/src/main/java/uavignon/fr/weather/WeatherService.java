package uavignon.fr.weather;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by uapv1202114 on 09/10/15.
 */
public class WeatherService extends IntentService {
    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String city = intent.getStringExtra("CITY");
        String country = intent.getStringExtra("COUNTRY");
        try {
            URL url;
            URLConnection con;
            InputStream is = null;
            final XMLResponseHandler xmlResponseHandler = new XMLResponseHandler();
            url = new URL("http://www.webservicex.net/globalweather.asmx/GetWeather?CityName=" +
                    URLEncoder.encode(city, "UTF-8") + "&CountryName=" +
                    URLEncoder.encode(country, "UTF-8"));
            con = url.openConnection();
            is = con.getInputStream();
            List<String> data = xmlResponseHandler.handleResponse(is, "UTF-8");
            ContentValues values = new ContentValues();
            values.put(WeatherDB.DATE, data.get(3));
            values.put(WeatherDB.WIND, data.get(0));
            values.put(WeatherDB.PRESSURE, data.get(2));
            values.put(WeatherDB.TEMP, data.get(1));

            int rowsDeleted = getContentResolver()
                    .update(WeatherContentProvider.getCityUri(city, country), values, null, null);
        } catch (Exception e) {
            Log.e("RefreshError", e.toString());
        }
    }
}
