package uavignon.fr.weather;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

        City city =  new City(name, country);
        Intent intent = new Intent();
        intent.putExtra(CityListActivity.CITY, city);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    public void cancel(View v) {
        finish();
    }
}
