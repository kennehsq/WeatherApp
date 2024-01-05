package com.example.dvtweather.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dvtweather.R
import com.example.dvtweather.WeatherViewModel
import com.example.dvtweather.ui.utilities.AppBar
import com.example.dvtweather.ui.utilities.GetText
import kotlinx.coroutines.launch

@Composable
fun FavPlacesScreen(
    viewModel: WeatherViewModel?,
    navController: NavController?,
    drawerInvoker: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState()}
        Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.sunny))
    ) {
        Scaffold(
            modifier = Modifier,
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                )
            },
            topBar = {
                AppBar(
                    onNavigationIconClick = {
                        drawerInvoker.invoke()
                    },
                    itemTitle = "Favorite Placess",
                    icon = Icons.Default.Menu,
                    color = Color.Transparent
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp,start = 24.dp, end = 24.dp)
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ){
                        GetText(value = "My Favourite places", textSize = 18.sp, textAlign = TextAlign.Center, color = Color.Black)

                        OutlinedButton(onClick = {
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Can't add places yet : We will notify you when feature is available",
                                    actionLabel = "Ok",
                                    duration = SnackbarDuration.Short
                                )
                                when(result) {
                                    SnackbarResult.ActionPerformed -> {
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                    }
                                    SnackbarResult.Dismissed -> {
                                        Unit
                                    }
                                }
                            }
                        })
                        {
                            GetText(value = "Add Place", textSize = 18.sp, textAlign = TextAlign.Center, color = Color.Black)
                        }
                    }
                }
            }
        )


    }
}

@Preview
@Composable
fun FavPlacesScreenPreview() {
    FavPlacesScreen(viewModel = null, navController = null, drawerInvoker = { })
}