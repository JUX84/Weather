package uavignon.fr.weather;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Date;

public class CityView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);
        City c = (City)getIntent().getSerializableExtra(CityListActivity.CITY);
        TextView tv;
        tv = (TextView) this.findViewById(R.id.cityValue);
        tv.setText(c.name);
        tv = (TextView) this.findViewById(R.id.countryValue);
        tv.setText(c.country);
        tv = (TextView) this.findViewById(R.id.windValue);
        tv.setText(c.wind);
        tv = (TextView) this.findViewById(R.id.pressureValue);
        tv.setText(c.pressure);
        tv = (TextView) this.findViewById(R.id.tempValue);
        tv.setText(c.temp);
        tv = (TextView) this.findViewById(R.id.dateValue);
        tv.setText(c.date);
    }
}
