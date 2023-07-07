package com.ldy.ex3.POJO;

public class Weather {
    private String weather;
    private City city;
    private String quality;
    private String temperature;
    private String windDirection;
    private String windForce;
    private String date;

    public Weather(String weather, City city, String quality, String temperature, String windDirection, String windForce, String date) {
        this.weather = weather;
        this.city = city;
        this.quality = quality;
        this.temperature = temperature;
        this.windDirection = windDirection;
        this.windForce = windForce;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Weather() {
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindForce() {
        return windForce;
    }

    public void setWindForce(String windForce) {
        this.windForce = windForce;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "weather='" + weather + '\'' +
                ", city=" + city +
                ", quality='" + quality + '\'' +
                ", temperature='" + temperature + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", windForce='" + windForce + '\'' +
                '}';
    }
}
