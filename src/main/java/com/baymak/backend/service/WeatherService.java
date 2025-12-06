package com.baymak.backend.service;

import com.baymak.backend.dto.WeatherResponseDto;

public interface WeatherService {
    WeatherResponseDto getWeatherByCity(String city);
}

