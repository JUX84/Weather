package uavignon.fr.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class WeatherDB extends SQLiteOpenHelper {

    public static final String TABLE = "weather";
    public static final String COUNTRY = "COUNTRY";
    public static final String CITY = "CITY_NAME";
    public static final String DATE = "DATE";
    public static final String WIND = "WIND";
    public static final String PRESSURE = "PRESSURE";
    public static final String TEMP = "TEMP";
    private static final String ID = "_id";

    public WeatherDB(Context context) {
        super(context, "WeatherDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE + "("
                + ID + " INTEGER PRIMARY KEY,"
                + COUNTRY + " TEXT,"
                + CITY + " TEXT,"
                + DATE + " TEXT,"
                + WIND + " TEXT,"
                + PRESSURE + " TEXT,"
                + TEMP + " TEXT,"
                + "UNIQUE(" + CITY + "," + COUNTRY + ")"
                + ")";
        db.execSQL(query);
    }

    public long addCity(String city, String country) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CITY, city);
        values.put(COUNTRY, country);

        long ret = db.insert(TABLE, null, values);

        return ret;
    }

    public int deleteCity(String city, String country) {
        SQLiteDatabase db = getWritableDatabase();
        int ret = db.delete(TABLE, CITY + "=? AND " + COUNTRY + "=?",
                new String[]{city, country});
        return ret;
    }

    public int updateCity(String city, String country, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        int ret = db.update(TABLE, values, CITY + "=? AND " + COUNTRY + "=?",
                new String[]{city, country});
        return ret;
    }

    public Cursor getCity(String city, String country) {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(TABLE, null, CITY + "=? AND " + COUNTRY + "=?",
                new String[]{city, country}, null, null, null, null);
    }

    public Cursor getAllCities() {
        SQLiteDatabase db = getReadableDatabase();
        final String selectQuery = "SELECT * FROM " + TABLE;

        return db.rawQuery(selectQuery, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE);
            onCreate(db);
        }
    }

    /*public List<City> getAllCities() {
        SQLiteDatabase db = getReadableDatabase();
        List<City> cityList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City(cursor.getString(cursor.getColumnIndex(CITY)),
                        cursor.getString(cursor.getColumnIndex(COUNTRY)));
                cityList.add(city);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return cityList;
    }*/
}
