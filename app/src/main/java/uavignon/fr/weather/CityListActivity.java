package uavignon.fr.weather;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class CityListActivity extends ListActivity {

    ArrayList<City> cityList;
    public static final String CITY = "uavignon.fr.city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityList = new ArrayList<>();
        cityList.add(new City("Marseille", "France"));

        ArrayAdapter<City> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, cityList);
        setListAdapter(adapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                new AlertDialog.Builder(CityListActivity.this)
                        .setMessage(R.string.remove_city)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cityList.remove(pos);
                                ((ArrayAdapter)(getListAdapter())).notifyDataSetChanged();
                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        Intent intent = new Intent(this, CityView.class);
        intent.putExtra(CITY, cityList.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_city) {
            Intent intent = new Intent(this, AddCityActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
