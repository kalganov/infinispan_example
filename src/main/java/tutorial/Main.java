package tutorial;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import java.io.IOException;

public class Main {

    static final String[] locations = {"Rome, Italy", "Como, Italy", "Basel, Switzerland", "Bern, Switzerland",
            "London, UK", "Newcastle, UK", "Bucarest, Romania", "Cluj-Napoca, Romania", "Ottawa, Canada",
            "Toronto, Canada", "Lisbon, Portugal", "Porto, Portugal", "Raleigh, USA", "Washington, USA"};

    public static void fetchWeather(WeatherService weatherService) {
        System.out.println("---- Fetching weather information ----");
        long start = System.currentTimeMillis();
        for (String location : locations) {
            LocationWeather weather = weatherService.getWeatherForLocation(location);
            System.out.printf("%s - %s\n", location, weather);
        }
        System.out.printf("---- Fetched in %dms ----\n", System.currentTimeMillis() - start);
    }

    public static void main(String[] args) throws IOException {
        EmbeddedCacheManager cacheManager = new DefaultCacheManager(Main.class.getResourceAsStream("/weatherapp-infinispan.xml"));


        Cache<String, LocationWeather> cache = cacheManager.getCache("weather");
        WeatherService weatherService = new RandomWeatherService(cache);

        System.out.println("---- Waiting for cluster to form ----");

        fetchWeather(weatherService);
        fetchWeather(weatherService);

    }

}