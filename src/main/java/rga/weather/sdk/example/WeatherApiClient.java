package rga.weather.sdk.example;

import lombok.extern.java.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Log
public class WeatherApiClient {

    private final String apiKey;
    private final Map<String, WeatherData> cache;
    private Timer timer;

    public WeatherApiClient(String apiKey, Mode mode) {
        this.apiKey = apiKey;
        this.cache = new HashMap<>();
        if (mode == Mode.POLLING) {
            startPolling();
        }
    }

    public WeatherData getWeather(String cityName) throws Exception {
        if (cache.containsKey(cityName) && isCacheValid(cache.get(cityName))) {
            return cache.get(cityName);
        }
        return fetchWeather(cityName);
    }

    private WeatherData fetchWeather(String cityName) throws Exception {
        String urlString = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s",
                cityName, apiKey);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");


        if (conn.getResponseCode() != 200) {
            throw new WeatherApiClientException("Failed to fetch weather data: " + conn.getResponseMessage());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        WeatherData data = parseWeatherData(jsonResponse);
        cacheWeatherData(cityName, data);
        return data;
    }

    private WeatherData parseWeatherData(JSONObject json) {
        JSONObject main = json.getJSONObject("main");
        JSONObject wind = json.getJSONObject("wind");
        JSONObject sys = json.getJSONObject("sys");

        WeatherData data = new WeatherData();

        data.setMainWeather(json.getJSONArray("weather").getJSONObject(0).getString("main"));
        data.setDescription(json.getJSONArray("weather").getJSONObject(0).getString("description"));
        data.setTemperature(main.getDouble("temp"));
        data.setFeelsLike(main.getDouble("feels_like"));
        data.setVisibility(json.getInt("visibility"));
        data.setWindSpeed(wind.getDouble("speed"));
        data.setSunrise(sys.getLong("sunrise"));
        data.setSunset(sys.getLong("sunset"));
        data.setCityName(json.getString("name"));
        return data;
    }

    private void cacheWeatherData(String cityName, WeatherData data) {
        int cacheLimit = 10;                            // in accordance with the condition
        if (cache.size() >= cacheLimit) {
            cache.remove(cache.keySet().iterator().next()); // to remove oldest entry
        }
        cache.put(cityName, data);
    }

    private boolean isCacheValid(WeatherData data) {
        long cacheDuration = 10 * 60 * 1000;    // in accordance with the condition
        return (System.currentTimeMillis() - data.getTimestamp()) < cacheDuration;
    }

    private void startPolling () {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (String city : cache.keySet()) {
                    try {
                        fetchWeather(city);
                    } catch (Exception e) {
                        log.severe("Failed to fetch weather");
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 600000); // // in accordance with the condition - to poll every 10 minutes
    }

    public void stopPolling() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void deleteCity(String cityName) {
        cache.remove(cityName);
    }
}
