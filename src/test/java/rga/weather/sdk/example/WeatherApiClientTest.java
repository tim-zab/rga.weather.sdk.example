package rga.weather.sdk.example;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Basic test cases for WeatherApiClient SDK")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WeatherApiClientTest {

    private WeatherApiClient client;
    private String apiKey = "e58980114f9e8520adb2a0499e27dc8d";

    @BeforeEach
    public void setUp() {
        client = new WeatherApiClient(apiKey, Mode.ON_DEMAND);
    }

    @Test
    @Order(30)
    @DisplayName("Weather data caching test")
    public void testCacheWeatherData() throws Exception {
        client.getWeather("Madrid");
        WeatherData cachedData = client.getWeather("Madrid");
        assertNotNull(cachedData);
    }

    @Test
    @Order(20)
    @DisplayName("Trying to get weather when location does not exist")
    public void testGetWeather_InvalidCity() {
        Exception exception = assertThrows(WeatherApiClientException.class, () -> {
            client.getWeather("InvalidCity");
        });
        assertTrue(exception.getMessage().contains("Failed to fetch weather data"));
    }

    @Test
    @Order(10)
    @DisplayName("Getting weather when location is valid")
    public void testGetWeather_ValidCity() throws Exception {
        WeatherData weatherData = client.getWeather("Paris");
        assertNotNull(weatherData);
        assertEquals("Paris", weatherData.getCityName());
    }

    @Test
    @Order(40)
    @DisplayName("Polling mode test")
    public void testPollingMode() {
        WeatherApiClient pollingClient = new WeatherApiClient(apiKey, Mode.POLLING);
        assertNotNull(pollingClient);
        pollingClient.stopPolling();
    }
}

