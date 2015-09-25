package uavignon.fr.weather;

/**
 * Created by uapv1202114 on 25/09/15.
 */
public class City {
    private String name;
    private String country;
    private long lastUpdate;
    private int wind;
    private int pressure;
    private int temp;

    public City(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}
