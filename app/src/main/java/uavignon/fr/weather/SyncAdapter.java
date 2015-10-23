package uavignon.fr.weather;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

class SyncAdapter extends AbstractThreadedSyncAdapter {
    public SyncAdapter(Context context) {
        super(context, true);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        String city = bundle.getString("CITY");
        String country = bundle.getString("COUNTRY");
        if (city != null && country != null) {
            syncCity(city, country, contentProviderClient);
        } else {
            try {
                Cursor cursor = contentProviderClient.query(WeatherContentProvider.CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        city = cursor.getString(cursor.getColumnIndex(WeatherDB.CITY));
                        country = cursor.getString(cursor.getColumnIndex(WeatherDB.COUNTRY));
                        syncCity(city, country, contentProviderClient);
                    }
                }
                assert cursor != null;
                cursor.close();
            } catch (Exception e) {
                Log.e("RefreshCursorError", e.toString());
            }
        }
    }

    private void syncCity(String city, String country, ContentProviderClient contentProviderClient) {
        try {
            URL url;
            URLConnection con;
            InputStream is;
            final XMLResponseHandler xmlResponseHandler = new XMLResponseHandler();
            url = new URL("http://www.webservicex.net/globalweather.asmx/GetWeather?CityName=" +
                    URLEncoder.encode(city, "UTF-8") + "&CountryName=" +
                    URLEncoder.encode(country, "UTF-8"));
            con = url.openConnection();
            is = con.getInputStream();
            List<String> data = xmlResponseHandler.handleResponse(is);
            ContentValues values = new ContentValues();
            values.put(WeatherDB.DATE, data.get(3));
            values.put(WeatherDB.WIND, data.get(0));
            values.put(WeatherDB.PRESSURE, data.get(2));
            values.put(WeatherDB.TEMP, data.get(1));

            contentProviderClient.update(WeatherContentProvider.getCityUri(city, country), values, null, null);
        } catch (Exception e) {
            Log.e("RefreshError", e.toString());
        }
    }
}
