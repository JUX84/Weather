package uavignon.fr.weather;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CityView extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String city;
    private String country;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);

        uri = getIntent().getParcelableExtra(CityListActivity.CITY);

        getLoaderManager().initLoader(0, null, this);

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            city = cursor.getString(cursor.getColumnIndex(WeatherDB.CITY));
            country = cursor.getString(cursor.getColumnIndex(WeatherDB.COUNTRY));
        } else {
            finish();
            return;
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
        cursor.close();
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
            /*Intent serviceIntent = new Intent(this, WeatherService.class);
            serviceIntent.putExtra("CITY", city);
            serviceIntent.putExtra("COUNTRY", country);
            startService(serviceIntent);*/

            Bundle bundle = new Bundle();
            bundle.putString("CITY", city);
            bundle.putString("COUNTRY", country);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            ContentResolver.requestSync(CityListActivity.mAccount, WeatherContentProvider.AUTHORITY, bundle);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            data.moveToFirst();
            TextView tv;
            tv = (TextView) this.findViewById(R.id.windValue);
            tv.setText(data.getString(data.getColumnIndex(WeatherDB.WIND)));
            tv = (TextView) this.findViewById(R.id.pressureValue);
            tv.setText(data.getString(data.getColumnIndex(WeatherDB.PRESSURE)));
            tv = (TextView) this.findViewById(R.id.tempValue);
            tv.setText(data.getString(data.getColumnIndex(WeatherDB.TEMP)));
            tv = (TextView) this.findViewById(R.id.dateValue);
            tv.setText(data.getString(data.getColumnIndex(WeatherDB.DATE)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
