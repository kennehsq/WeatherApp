package com.example.dvtweather

import android.util.Log
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dvtweather.data.api.dto.WeatherDTO
import com.example.dvtweather.data.api.dto.WeatherForecatsDTO
import com.example.dvtweather.data.domain.repositories.WeatherRepository
import com.example.dvtweather.ui.data.MainWeatherItem
import com.example.dvtweather.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    var lat = mutableDoubleStateOf(0.0)
    var long = mutableDoubleStateOf(0.0)

    private val _uiState = MutableLiveData(WeatherScreensState())
    val uiState = _uiState

    private fun onEvent(event: WeatherEvent) {
        when (event) {
            is WeatherEvent.Loading -> {
                _uiState.value = _uiState.value?.copy(isLoading = event.isLoading)
            }
            is WeatherEvent.GetWeather -> {
                _uiState.value = _uiState.value?.copy(weather = event.weather)
            }
            is WeatherEvent.GetWeatherForeCast -> {
                _uiState.value = _uiState.value?.copy(weatherForeCast = event.weatherForeCast)
            }
            is WeatherEvent.GetPlacesWeather -> {
                _uiState.value = _uiState.value?.copy(placesWeather = event.placeWeather)
            }
        }

    }

    fun updateLatLong(lat: Double, long: Double) {
        if(this.lat.doubleValue != lat && this.long.doubleValue != long) {
            this.lat.doubleValue = lat
            this.long.doubleValue = long
            getWeather()
        }
    }

    private var exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Unit
    }

    fun getWeather() {
        viewModelScope.launch(exceptionHandler) {
            onEvent(WeatherEvent.Loading(true))
            val requestForecast = weatherRepository.getForecast(
                lat = lat.value.toString(),
                long = long.value.toString(),
                appId = AppConstants.API_KEY,
            )

            if(requestForecast is WeatherForecatsDTO){
                Log.d("WeatherViewModel", "getWeather: success ${requestForecast.toString()}")
                onEvent(WeatherEvent.GetWeatherForeCast(requestForecast))
            }

            val requestWeather =
                weatherRepository.getWeather(
                    lat = lat.value.toString(),
                    long = long.value.toString(),
                    appId = AppConstants.API_KEY,
                )

            if(requestWeather is WeatherDTO){
                Log.d("WeatherViewModel", "getWeather: success ${requestWeather.toString()}")
                onEvent(WeatherEvent.GetWeather(requestWeather))
                onEvent(WeatherEvent.Loading(false))
            }
        }
    }

}

data class WeatherScreensState(
    val isLoading : Boolean = false,
    var weather : WeatherDTO? = null,
    var weatherForeCast : WeatherForecatsDTO? = null,
    var placesWeather: WeatherDTO? = null,
)

sealed class WeatherEvent {
    data class Loading(val isLoading: Boolean) : WeatherEvent()
    data class GetWeather(val weather: WeatherDTO) : WeatherEvent()
    data class GetWeatherForeCast(val weatherForeCast: WeatherForecatsDTO) : WeatherEvent()
    data class GetPlacesWeather(val placeWeather: WeatherDTO) : WeatherEvent()
}