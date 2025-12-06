package com.baymak.backend.service.impl;

import com.baymak.backend.dto.WeatherResponseDto;
import com.baymak.backend.exception.BadRequestException;
import com.baymak.backend.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final RestTemplate restTemplate;

    // İstanbul koordinatları
    private static final double ISTANBUL_LATITUDE = 41.015137;
    private static final double ISTANBUL_LONGITUDE = 28.979530;
    private static final String OPEN_METEO_API_URL = "https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current_weather=true";

    @Override
    public WeatherResponseDto getWeatherByCity(String city) {
        try {
            log.info("Fetching weather data for city: {}", city);

            // Open-Meteo API çağrısı
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    OPEN_METEO_API_URL,
                    Map.class,
                    ISTANBUL_LATITUDE,
                    ISTANBUL_LONGITUDE
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                log.error("Failed to fetch weather data. Status: {}", response.getStatusCode());
                throw new BadRequestException("Failed to fetch weather data from external API");
            }

            Map<String, Object> responseBody = response.getBody();
            Map<String, Object> currentWeather = (Map<String, Object>) responseBody.get("current_weather");

            if (currentWeather == null) {
                log.error("Current weather data not found in API response");
                throw new BadRequestException("Weather data not available");
            }

            // DTO'ya mapping
            WeatherResponseDto weatherDto = WeatherResponseDto.builder()
                    .temperature(getDoubleValue(currentWeather, "temperature"))
                    .windspeed(getDoubleValue(currentWeather, "windspeed"))
                    .winddirection(getIntegerValue(currentWeather, "winddirection"))
                    .weathercode(getIntegerValue(currentWeather, "weathercode"))
                    .time(getStringValue(currentWeather, "time"))
                    .build();

            log.info("Successfully fetched weather data for city: {}", city);
            return weatherDto;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error while fetching weather data: {}", e.getMessage());
            throw new BadRequestException("External weather API returned an error: " + e.getMessage());
        } catch (RestClientException e) {
            log.error("Rest client error while fetching weather data: {}", e.getMessage());
            throw new BadRequestException("Failed to connect to weather API: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while fetching weather data: {}", e.getMessage(), e);
            throw new BadRequestException("An unexpected error occurred while fetching weather data: " + e.getMessage());
        }
    }

    private Double getDoubleValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return null;
    }

    private Integer getIntegerValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }
}

