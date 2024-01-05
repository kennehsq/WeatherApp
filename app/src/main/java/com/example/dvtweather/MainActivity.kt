package com.example.dvtweather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dvtweather.ui.data.MenuItem
import com.example.dvtweather.ui.screens.Routes
import com.example.dvtweather.ui.screens.WeatherNavigationGraph
import com.example.dvtweather.ui.utilities.AppBar
import com.example.dvtweather.ui.utilities.GetText
import com.example.dvtweather.ui.utilities.IndeterminateCircularIndicator
import com.example.dvtweather.ui.screens.PermissionScreen
import com.example.dvtweather.ui.screens.fusedLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: WeatherViewModel = hiltViewModel()
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val navController = rememberNavController()
            val items = listOf(
                MenuItem(
                    id = "home",
                    title = "Home",
                    contentDescription = "Go to home screen",
                    icon = Icons.Default.Home
                ),
                MenuItem(
                    id = "favourite",
                    title = "Favourite",
                    contentDescription = "Go to favorites screen",
                    icon = Icons.Default.Favorite
                )
            )
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Spacer(modifier = Modifier.height(16.dp))
                        items.forEach { item ->
                            NavigationDrawerItem(
                                label = {
                                    GetText(
                                        value = item.title,
                                        textSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color.Black
                                    )
                                },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        drawerState.isClosed
                                    }
                                    val route = when (item.id) {
                                        "home" -> Routes.WEATHER_SCREEN
                                        "favourite" -> Routes.FAV_PLACES_SCREEN
                                        else -> Routes.WEATHER_SCREEN
                                    }
                                    navController.navigate(route)
                                },
                                icon = {
                                    Icon(imageVector = item.icon, contentDescription = item.contentDescription)
                                }
                            )
                        }
                    }
                },
                gesturesEnabled = drawerState.isOpen,
            ) {
                SetUpUIContent(viewModel, navController
                ) {
                    scope.launch {
                        drawerState.open()
                    }
                }
                /*Scaffold(
                    modifier = Modifier,
                    topBar = {
                        AppBar(
                            onNavigationIconClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                            itemTitle = "Weather App",
                            icon = Icons.Default.Menu,
                            color = colorResource( id = R.color.cloudy)
                        )
                    },
                    content = { paddingValues ->
                        SetUpUIContent(paddingValues, viewModel, navController)
                    }
                )*/
            }
        }
    }

    @Composable
    fun SetUpUIContent(
        viewModel: WeatherViewModel,
        navController: NavHostController,
        drawerInvoked: () -> Unit
    ) {
        WeatherNavigationGraph(
            viewModel = viewModel,
            navController = navController,
            {
                drawerInvoked.invoke()
            }
        )
    }

}


