package rga.weather.sdk.example;

import lombok.Getter;
import lombok.Setter;

@Setter
public class WeatherData {

    private String mainWeather;
    private String description;
    private double temperature;
    private double feelsLike;
    private int visibility;
    private double windSpeed;
    private long sunrise;
    private long sunset;
    @Getter
    private String cityName;
    @Getter
    private long timestamp;

    public WeatherData() {
        this.timestamp = System.currentTimeMillis();
    }
}

