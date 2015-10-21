package uavignon.fr.weather;

import java.io.Serializable;

class City implements Serializable {
    private final String name;
    private final String country;
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
