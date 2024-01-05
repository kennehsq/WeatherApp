package com.example.dvtweather.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dvtweather.WeatherViewModel
import com.example.dvtweather.ui.utilities.IndeterminateCircularIndicator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


lateinit var fusedLocation: FusedLocationProviderClient
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(viewModel: WeatherViewModel,navController: NavHostController) {
    IndeterminateCircularIndicator(true)
    if (ActivityCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        val permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val permissionState = rememberMultiplePermissionsState(permissions = permissions)
        CheckWeatherPermission(viewModel = viewModel, permissionState = permissionState, navController = navController)
    }else{
        GetCurrentLocation(viewModel = viewModel,navController = navController)
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckWeatherPermission(viewModel: WeatherViewModel, permissionState: MultiplePermissionsState, navController: NavHostController) {
    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = {
            GetPermission(viewModel = viewModel, permissionState = permissionState, navController = navController)
        },
        permissionsNotAvailableContent = {

        }) {

    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetPermission(viewModel: WeatherViewModel, permissionState: MultiplePermissionsState, navController: NavHostController) {
    val lifeCycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionState.launchMultiplePermissionRequest()
                }
                else -> {

                }

            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IndeterminateCircularIndicator(true)
        permissionState.permissions.forEach { permission ->
            when (permission.permission) {
                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION -> {
                    if (permission.hasPermission) {
                        GetCurrentLocation(viewModel = viewModel,navController = navController)
                    } else {
                        Text(text = "Location permission denied")
                    }

                    if (permission.shouldShowRationale) {
                        Text(text = "Location permission is needed to get weather data")
                    }
                }
            }
        }
    }
}

@Composable
fun GetCurrentLocation(viewModel: WeatherViewModel,navController: NavHostController) {
    val fusedLocation = LocationServices.getFusedLocationProviderClient(LocalContext.current)
    if (ActivityCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    fusedLocation.lastLocation.addOnSuccessListener {
        if (it != null) {
            val lat = it.latitude
            val long = it.longitude
            viewModel.updateLatLong(lat, long)
            navController.navigate(Routes.WEATHER_SCREEN)
        }
    }
}


@Preview
@Composable
fun PermissionScreenPreview() {
    val viewModel: WeatherViewModel = hiltViewModel()
    PermissionScreen(viewModel = viewModel, navController = rememberNavController())
}