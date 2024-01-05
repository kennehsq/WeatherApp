package com.example.dvtweather.data.domain.repositories

import com.example.dvtweather.data.api.dto.WeatherDTO
import com.example.dvtweather.data.api.dto.WeatherForecatsDTO
import com.example.dvtweather.util.AppConstants

interface WeatherRepository {
    suspend fun getWeather(long: String, lat: String, appId: String = AppConstants.API_KEY): WeatherDTO

    suspend fun getForecast(long: String, lat: String, appId: String = AppConstants.API_KEY): WeatherForecatsDTO
}