package com.example.dvtweather.data.api.dto

data class WeatherForecatsDTO(
    val city: City?,
    val cnt: Int?,
    val cod: String?,
    val list: List<ForeCastItem>?,
    val message: Int?
)

data class ForeCastItem(
    val clouds: Clouds,
    val dt: Long,
    val dt_txt: String,
    val main: Main,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)