package com.baymak.backend.controller;

import com.baymak.backend.dto.WeatherResponseDto;
import com.baymak.backend.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Tag(name = "Weather", description = "Weather information operations")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    @Operation(
            summary = "Şehir için hava durumu bilgisi",
            description = "Belirtilen şehir için güncel hava durumu bilgilerini Open-Meteo API'den çeker. " +
                    "Şu anda İstanbul için hava durumu bilgisi sağlanmaktadır. " +
                    "Dönen bilgiler: sıcaklık, rüzgar hızı, rüzgar yönü, hava durumu kodu ve zaman bilgisini içerir."
    )
    public ResponseEntity<WeatherResponseDto> getWeather(@RequestParam(required = false, defaultValue = "Istanbul") String city) {
        WeatherResponseDto weather = weatherService.getWeatherByCity(city);
        return ResponseEntity.ok(weather);
    }
}

