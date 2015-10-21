package uavignon.fr.weather;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

public class WeatherContentProvider extends ContentProvider {
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int COUNTRY_SEGMENT = 1;
    private static final int CITY_SEGMENT = 2;
    private static final int WEATHER = 1;
    private static final int WEATHER_CITY = 2;
    private static final String AUTHORITY = "uavignon.fr.weather";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
            .authority(WeatherContentProvider.AUTHORITY)
            .appendEncodedPath(WeatherDB.TABLE)
            .build();

    static {
        uriMatcher.addURI(AUTHORITY, "weather", WEATHER);
        uriMatcher.addURI(AUTHORITY, "weather/*/*", WEATHER_CITY);
    }

    private WeatherDB db = null;

    public static Uri getCityUri(String city, String country) {
        return WeatherContentProvider.CONTENT_URI.buildUpon().appendPath(country).appendPath(city).build();
    }

    @Override
    public boolean onCreate() {
        db = new WeatherDB(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = uriMatcher.match(uri);

        Cursor result;

        switch (match) {
            case WEATHER:
                result = db.getAllCities();
                break;
            case WEATHER_CITY:
                List<String> pathSegments = uri.getPathSegments();
                String country = pathSegments.get(COUNTRY_SEGMENT);
                String city = pathSegments.get(CITY_SEGMENT);
                result = db.getCity(city, country);
                break;
            default:
                return null;
        }

        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + ".weather";
            case WEATHER_CITY:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + ".weather";
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != WEATHER_CITY)
            return null;

        List<String> pathSegments = uri.getPathSegments();
        String country = pathSegments.get(COUNTRY_SEGMENT);
        String city = pathSegments.get(CITY_SEGMENT);

        return (db.addCity(city, country) == -1) ? null : getCityUri(city, country);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) != WEATHER_CITY)
            return 0;

        List<String> pathSegments = uri.getPathSegments();
        String country = pathSegments.get(COUNTRY_SEGMENT);
        String city = pathSegments.get(CITY_SEGMENT);

        return db.deleteCity(city, country);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) != WEATHER_CITY)
            return 0;

        List<String> pathSegments = uri.getPathSegments();
        String country = pathSegments.get(COUNTRY_SEGMENT);
        String city = pathSegments.get(CITY_SEGMENT);

        getContext().getContentResolver().notifyChange(uri, null);

        return db.updateCity(city, country, values);
    }
}
