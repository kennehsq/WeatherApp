package com.example.dvtweather.data.api.remote

import com.example.dvtweather.data.api.dto.WeatherDTO
import com.example.dvtweather.data.api.dto.WeatherForecatsDTO
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query


interface WeatherAPI {

    @POST("weather")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric")
    : WeatherDTO

    @POST("forecast")
    suspend fun getForeCast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric"
    ) : WeatherForecatsDTO
}