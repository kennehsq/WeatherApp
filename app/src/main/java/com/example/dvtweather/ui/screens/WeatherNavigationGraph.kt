package com.example.dvtweather.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dvtweather.WeatherViewModel

@Composable
fun WeatherNavigationGraph(
    viewModel: WeatherViewModel,
    navController: NavHostController = rememberNavController(),
    drawerInvokation:() -> Unit
){
    NavHost(navController = navController, startDestination = Routes.PERMISSION_SCREEN) {

        composable(Routes.WEATHER_SCREEN){
            WeatherScreen(viewModel = viewModel, navController = navController, invokeDrawer = {
                drawerInvokation.invoke()
            })
        }

        composable(Routes.FAV_PLACES_SCREEN){
            FavPlacesScreen(viewModel = viewModel,  navController = navController, drawerInvoker = {
                drawerInvokation.invoke()
            })
        }

        composable(Routes.PERMISSION_SCREEN){
            PermissionScreen(viewModel = viewModel, navController = navController)
        }
    }
}