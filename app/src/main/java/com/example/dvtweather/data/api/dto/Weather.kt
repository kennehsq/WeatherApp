package com.example.dvtweather.data.api.dto

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)