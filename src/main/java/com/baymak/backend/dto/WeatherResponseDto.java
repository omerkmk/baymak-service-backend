package com.baymak.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponseDto {
    private Double temperature;
    private Double windspeed;
    private Integer winddirection;
    private Integer weathercode;
    private String time;
}

