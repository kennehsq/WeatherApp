package com.example.dvtweather

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dvtweather.ui.data.MenuItem
import com.example.dvtweather.ui.screens.Routes
import com.example.dvtweather.ui.screens.WeatherNavigationGraph
import com.example.dvtweather.ui.utilities.GetText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var pressedTime: Long = 0L
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
            navController = navController
        ) {
            drawerInvoked.invoke()
        }
    }

    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(baseContext,"Press back again to exit",Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
    }

}


