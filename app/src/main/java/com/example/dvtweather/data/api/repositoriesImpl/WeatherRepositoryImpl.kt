package com.example.dvtweather.data.api.repositoriesImpl


import com.example.dvtweather.data.api.dto.WeatherDTO
import com.example.dvtweather.data.api.dto.WeatherForecatsDTO
import com.example.dvtweather.data.api.remote.WeatherAPI
import com.example.dvtweather.data.domain.repositories.WeatherRepository
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: WeatherAPI): WeatherRepository {
    /**
     * When fetching Items a DTO mapper is to be applied to switch items to domain models.
     * For this assessment I will not be using a DTO mapper as the data is not complex.
     * in the event RoomDB was being used then we would have had to use the DTO mapper
     */
    override suspend fun getWeather(long: String, lat: String, appId: String): WeatherDTO = api.getWeather(lon =long, lat = lat, appid = appId)

    override suspend fun getForecast(long: String, lat: String, appId: String): WeatherForecatsDTO = api.getForeCast(lon =long, lat = lat, appid = appId)
}