package uavignon.fr.weather;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                                ((ArrayAdapter) (getListAdapter())).notifyDataSetChanged();
                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                return true;
            }
        });
        new refreshData().execute(cityList);
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
        } else if (id == R.id.action_refresh) {
            new refreshData().execute(cityList);
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh(List<City> cities) {
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            final XMLResponseHandler xmlResponseHandler = new XMLResponseHandler();
            for(final City c : cities) {
                String url = "http://www.webservicex.net/globalweather.asmx/GetWeather?CityName=" + URLEncoder.encode(c.name, "UTF-8") + "&CountryName=" + URLEncoder.encode(c.country, "UTF-8");
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<String> data = xmlResponseHandler.handleResponse(new ByteArrayInputStream(response.getBytes()), "UTF-8");
                            c.wind = data.get(0);
                            c.temp = data.get(1);
                            c.pressure = data.get(2);
                            c.date = data.get(3);
                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RefreshErrorResponse", error.toString());
                    }
                });
                queue.add(stringRequest);
            }
        } catch (Exception e) {
            Log.e("RefreshError", e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                City city = (City)data.getSerializableExtra(CITY);
                cityList.add(city);
                ((ArrayAdapter)(getListAdapter())).notifyDataSetChanged();
                new refreshData().execute(cityList);
            }
        }
    }

    private class refreshData extends AsyncTask<List<City>, Void, Void>
    {

        @Override
        protected Void doInBackground(List<City>... cities)
        {
            refresh(cities[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Toast.makeText(CityListActivity.this,
                    getString(R.string.refresh_done), Toast.LENGTH_LONG) .show();
        }
    }
}
