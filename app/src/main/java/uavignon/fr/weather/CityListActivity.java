package uavignon.fr.weather;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CityListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String CITY = "uavignon.fr.city";

    public static final String ACCOUNT_TYPE = "webservicex.net";
    public static final String ACCOUNT = "default";

    public static Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccount = CreateSyncAccount(this);

        ContentResolver contentResolver = getContentResolver();
        contentResolver.removePeriodicSync(mAccount, WeatherContentProvider.AUTHORITY,
                Bundle.EMPTY);
        contentResolver.setSyncAutomatically(mAccount, WeatherContentProvider.AUTHORITY, true);
        contentResolver.addPeriodicSync(mAccount, WeatherContentProvider.AUTHORITY, Bundle.EMPTY, getSyncFrequency());

        getLoaderManager().initLoader(0, null, this);

        new WeatherDB(this);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, null,
                new String[]{WeatherDB.CITY, WeatherDB.COUNTRY},
                new int[]{android.R.id.text1, android.R.id.text2}, 0);
        setListAdapter(adapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String city = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                final String country = ((TextView) view.findViewById(android.R.id.text2)).getText().toString();

                new AlertDialog.Builder(CityListActivity.this)
                        .setMessage(R.string.remove_city)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getContentResolver()
                                        .delete(WeatherContentProvider.getCityUri(city, country), null, null);
                                getLoaderManager().restartLoader(0, null, CityListActivity.this);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        final String city = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
        final String country = ((TextView) view.findViewById(android.R.id.text2)).getText().toString();

        Intent intent = new Intent(this, CityView.class);
        intent.putExtra(CITY, WeatherContentProvider.getCityUri(city, country));
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
        } else if (id == R.id.menu_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, 1);
        }/* else if (id == R.id.action_refresh) {
            Intent serviceIntent = new Intent(this, WeatherService.class);
            startService(serviceIntent);
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                getLoaderManager().restartLoader(0, null, this);
            }
        }
        if (requestCode == 1) {
            ContentResolver contentResolver = getContentResolver();
            contentResolver.removePeriodicSync(mAccount, WeatherContentProvider.AUTHORITY,
                    Bundle.EMPTY);
            contentResolver.setSyncAutomatically(mAccount, WeatherContentProvider.AUTHORITY, true);
            contentResolver.addPeriodicSync(mAccount, WeatherContentProvider.AUTHORITY, Bundle.EMPTY, getSyncFrequency());
        }
    }

    public long getSyncFrequency() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        long syncFrequency = Long.parseLong(sharedPrefs.getString("prefSyncFrequency", "1800"));
        Log.i("syncFreq", String.valueOf(syncFrequency));
        return syncFrequency;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, WeatherContentProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((SimpleCursorAdapter) getListAdapter()).changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);

        accountManager.addAccountExplicitly(newAccount, null, null);

        return newAccount;
    }
}
