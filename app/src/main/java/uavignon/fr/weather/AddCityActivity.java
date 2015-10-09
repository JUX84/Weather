package uavignon.fr.weather;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AddCityActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
    }

    public void save(View v) {
        TextView cityview = (TextView) findViewById(R.id.editCity);
        String name = cityview.getText().toString();
        TextView countryview = (TextView) findViewById(R.id.editCountry);
        String country = countryview.getText().toString();

        boolean error = false;
        if (name.isEmpty()) {
            error = true;
            cityview.setHintTextColor(Color.RED);
        } else {
            cityview.setHintTextColor(Color.LTGRAY);
        }
        if (country.isEmpty()) {
            error = true;
            countryview.setHintTextColor(Color.RED);
        } else {
            countryview.setHintTextColor(Color.LTGRAY);
        }

        if (error)
            return;

        getContentResolver().insert(WeatherContentProvider.getCityUri(name, country), null);

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void cancel(View v) {
        finish();
    }
}
