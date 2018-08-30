package model;

public class Weather {
    public Place place;
    public Temperature temperature;
    public String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
