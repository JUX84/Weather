package uavignon.fr.weather;

import java.io.Serializable;

public class City implements Serializable {
    public String name;
    public String country;
    public String date;
    public String wind;
    public String pressure;
    public String temp;

    public City(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String toString() {
        return name + " (" + country + ")";
    }
}
