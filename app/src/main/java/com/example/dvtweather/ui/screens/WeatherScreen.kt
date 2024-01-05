package com.example.dvtweather.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.dvtweather.R
import com.example.dvtweather.WeatherViewModel
import com.example.dvtweather.data.api.dto.WeatherDTO
import com.example.dvtweather.data.api.dto.WeatherForecatsDTO
import com.example.dvtweather.ui.data.MainWeatherItem
import com.example.dvtweather.ui.utilities.ForeCastItem
import com.example.dvtweather.ui.utilities.GetTempDistribution
import com.example.dvtweather.ui.utilities.GetTopBox
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    navController: NavHostController,
    invokeDrawer: () -> Unit
) {
    val state by viewModel.uiState.observeAsState()
    var loading: Boolean by remember { mutableStateOf(false) }
    var weather: WeatherDTO by remember {
        mutableStateOf(
            WeatherDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        )
    }

    var weatherForeCast: WeatherForecatsDTO by remember {
        mutableStateOf(WeatherForecatsDTO(null, null, null, null, null))
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (!loading) {
            state?.weather?.let { returnedWeather ->
                state?.weatherForeCast?.let {
                    weatherForeCast = it
                    weather = returnedWeather
                    SetupWeatherAppUI(weather, weatherForeCast, onNavDrawerInvocked = {
                        invokeDrawer.invoke()
                    })
                }
            }
        }

    }
}


@Composable
fun SetupWeatherAppUI(weather: WeatherDTO?, weatherForeCast: WeatherForecatsDTO?, onNavDrawerInvocked: () -> Unit) {
    var backgroundColor: Int by remember { mutableStateOf(R.color.sunny) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = backgroundColor))
    ) {
        var mainWeatherItem by remember {
            mutableStateOf(
                MainWeatherItem(
                    condition = weather?.weather?.get(0)?.main.toString(),
                    temp = weather?.main?.temp.toString(),
                    backgroundColor = R.color.cloudy,
                    backgroundImage = R.drawable.forest_cloudy
                )
            )
        }
        weather?.let{
            mainWeatherItem = getWeatherBackGroundColor(it)
            GetTopBox(mainWeatherItem, onMenuIconClick = {
                    onNavDrawerInvocked.invoke()
                }
            )
            backgroundColor = mainWeatherItem.backgroundColor
        }
        GetTempDistribution(weather?.main)

        Divider()

        weatherForeCast?.list?.let { items ->
            LazyColumn{
                items(items){foreCastItem ->
                    ForeCastItem(foreCastItem)
                }
            }
        }


        weatherForeCast?.let { weatherForecaseItem ->
            weatherForecaseItem.list?.let { list ->
                list.forEach { weatherItem ->
                    ForeCastItem(weatherItem)
                }
            }
        }
    }
}

fun getWeatherBackGroundColor(weatherDTO: WeatherDTO): MainWeatherItem {
    when(weatherDTO.weather?.get(0)?.main) {
        "Clouds" -> {
            return MainWeatherItem(
                condition = weatherDTO.weather?.get(0)?.main.toString(),
                temp = weatherDTO.main?.temp.toString(),
                backgroundColor = R.color.cloudy,
                backgroundImage = R.drawable.forest_cloudy

            )
        }
        "Rain" -> {
            return MainWeatherItem(
                condition = weatherDTO.weather?.get(0)?.main.toString(),
                temp = weatherDTO.main?.temp.toString(),
                backgroundColor = R.color.rainy,
                backgroundImage = R.drawable.forest_rainy
            )
        }
        "Clear" -> {
            return MainWeatherItem(
                condition = weatherDTO.weather?.get(0)?.main.toString(),
                temp = weatherDTO.main?.temp.toString(),
                backgroundColor = R.color.sunny,
                backgroundImage = R.drawable.forest_sunny
            )
        }
        else -> {
            return MainWeatherItem(
                condition = weatherDTO.weather?.get(0)?.main.toString(),
                temp = weatherDTO.main?.temp.toString(),
                backgroundColor = R.color.cloudy,
                backgroundImage = R.drawable.forest_cloudy

            )
        }
    }
}

@Composable
fun showScaffold() {

}


@Preview
@Composable
fun WeatherScreenPreview() {
    SetupWeatherAppUI(null, null, {})
}