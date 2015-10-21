package uavignon.fr.weather;

import android.app.Activity;
import android.app.IntentService;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Date;

public class CityView extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String city;
    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);

        getLoaderManager().initLoader(0, null, this);

        Uri uri = getIntent().getParcelableExtra(CityListActivity.CITY);
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            city = cursor.getString(cursor.getColumnIndex(WeatherDB.CITY));
            country = cursor.getString(cursor.getColumnIndex(WeatherDB.COUNTRY));
        } else {
            finish();
        }
        TextView tv;
        tv = (TextView) this.findViewById(R.id.cityValue);
        tv.setText(city);
        tv = (TextView) this.findViewById(R.id.countryValue);
        tv.setText(country);
        tv = (TextView) this.findViewById(R.id.windValue);
        tv.setText(cursor.getString(cursor.getColumnIndex(WeatherDB.WIND)));
        tv = (TextView) this.findViewById(R.id.pressureValue);
        tv.setText(cursor.getString(cursor.getColumnIndex(WeatherDB.PRESSURE)));
        tv = (TextView) this.findViewById(R.id.tempValue);
        tv.setText(cursor.getString(cursor.getColumnIndex(WeatherDB.TEMP)));
        tv = (TextView) this.findViewById(R.id.dateValue);
        tv.setText(cursor.getString(cursor.getColumnIndex(WeatherDB.DATE)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_city, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            Intent serviceIntent = new Intent(this, WeatherService.class);
            serviceIntent.putExtra("CITY", city);
            serviceIntent.putExtra("COUNTRY", country);
            startService(serviceIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int i = 0;
        return new CursorLoader(this, WeatherContentProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int i = 0;
        if (data != null) {
            data.moveToFirst();
            city = data.getString(data.getColumnIndex(WeatherDB.CITY));
            country = data.getString(data.getColumnIndex(WeatherDB.COUNTRY));
        } else {
            return;
        }
        TextView tv;
        tv = (TextView) this.findViewById(R.id.cityValue);
        tv.setText(city);
        tv = (TextView) this.findViewById(R.id.countryValue);
        tv.setText(country);
        tv = (TextView) this.findViewById(R.id.windValue);
        tv.setText(data.getString(data.getColumnIndex(WeatherDB.WIND)));
        tv = (TextView) this.findViewById(R.id.pressureValue);
        tv.setText(data.getString(data.getColumnIndex(WeatherDB.PRESSURE)));
        tv = (TextView) this.findViewById(R.id.tempValue);
        tv.setText(data.getString(data.getColumnIndex(WeatherDB.TEMP)));
        tv = (TextView) this.findViewById(R.id.dateValue);
        tv.setText(data.getString(data.getColumnIndex(WeatherDB.DATE)));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int i = 0;
    }
}
