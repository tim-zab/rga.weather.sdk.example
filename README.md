# WeatherApiClient SDK  

## Overview  
The WeatherApiClient SDK provides an easy way to access weather data from the OpenWeather API 
( https://openweathermap.org/api ). 
It supports both on-demand and polling modes for fetching weather data. 

## Installation 
To use the SDK, include it in your project as a dependency: 

<groupId>rga.weather</groupId>
<artifactId>sdk-example</artifactId>
<version>0.0.1-SNAPSHOT</version>

## Usage   

### Initialization 
WeatherApiClient client = new WeatherApiClient("your_api_key", "on-demand");

### Get Weather Data 
To get weather data can be used the following: 

try { 
    WeatherData weather = client.getWeather("Moscow"); 
    System.out.println("Weather in " + weather.getCityName() + ": " + weather.getMainWeather()); 
} catch (Exception e) {
    e.printStackTrace(); 
} 

### Delete City from Cache 
client.deleteCity("Helsinki"); 

### Polling Mode 
To use polling mode, initialize the client with "polling": 

WeatherApiClient pollingClient = new WeatherApiClient("your_api_key", "polling"); 

To stop polling, can be used method: 

pollingClient.stopPolling(); 

### Error Handling 
The SDK throws exceptions for various error conditions, such as invalid API keys or network issues. 
If connection gets no success code WeatherApiClientException will be thrown. 

### Unit Tests 
The SDK is partly covered with unit tests (some basic cases) using junit. 
Use JUnit to run the tests. 
