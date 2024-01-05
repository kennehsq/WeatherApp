package com.example.dvtweather.ui.utilities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dvtweather.R
import com.example.dvtweather.data.api.dto.Clouds
import com.example.dvtweather.data.api.dto.ForeCastItem
import com.example.dvtweather.data.api.dto.Main
import com.example.dvtweather.data.api.dto.Rain
import com.example.dvtweather.data.api.dto.Sys
import com.example.dvtweather.data.api.dto.Weather
import com.example.dvtweather.data.api.dto.Wind
import com.example.dvtweather.ui.data.MainWeatherItem
import com.example.dvtweather.ui.data.MenuItem
import java.text.SimpleDateFormat
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit,
    itemTitle: String,
    icon: ImageVector,
    color: Color
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        colors = topAppBarColors(
            containerColor = color
        ),
        title = {
            GetText(
                value = itemTitle,
                textSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Return to home page",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun IndeterminateCircularIndicator(loading: Boolean) {
    var loading by remember { mutableStateOf(loading) }

    if (!loading) return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.cloudy)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = colorResource(id = R.color.white),
            trackColor = colorResource(id = R.color.teal_700),
        )
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier,
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(modifier.padding(16.dp)) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription,
                    tint = Color.White
                )

                GetText(
                    value = item.title,
                    textSize = 16.sp,
                    textAlign = TextAlign.Start,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun GetTopBox(weatherItem: MainWeatherItem, onMenuIconClick: () -> Unit) {
    var weatherItem by remember { mutableStateOf(weatherItem) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(id = weatherItem.backgroundImage),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
        )

       Icon(
            modifier = Modifier
                .height(60.dp)
                .width(60.dp)
                .padding(16.dp)
                .clickable { onMenuIconClick.invoke() },
            painter = painterResource(id = R.drawable.ic_hamburger_menu),
            tint = Color.White,
            contentDescription = "Gradient",
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GetText(
                value = "${weatherItem.temp}\u00B0",
                textSize = 48.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )

            GetText(
                value = "${weatherItem.condition}",
                textSize = 24.sp,
                textAlign = TextAlign.Center,
            )

        }
    }
}

@Composable
fun GetText(
    value: String,
    textSize: TextUnit,
    color: Color = Color.White,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign?,
    padding: PaddingValues = PaddingValues(4.dp)
) {
    Text(
        modifier =
        Modifier
            .padding(paddingValues = padding),
        text = value,
        textAlign = textAlign,
        style = TextStyle(
            color = color,
            fontSize = textSize,
            fontWeight = fontWeight
        )
    )
}

@Composable
fun GetTempDistribution(main: Main?) {
    var temp by remember { mutableStateOf(0.0)}
    var tempMax by remember { mutableStateOf(0.0)}
    var tempMin by remember { mutableStateOf(0.0)}

    main?.temp?.let { temp = it }
    main?.temp_max?.let { tempMax = it }
    main?.temp_min?.let { tempMin = it }

    Row(
        modifier = Modifier
            .height(96.dp)
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GetText(
                value = "${tempMin}°",
                textSize = 20.sp,
                textAlign = TextAlign.Center
            )

            GetText(
                value = "min",
                textSize = 16.sp,
                textAlign = TextAlign.Center,
                padding = PaddingValues(bottom = 4.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GetText(
                value = "$temp°",
                textSize = 20.sp,
                textAlign = TextAlign.Center
            )

            GetText(
                value = "current",
                textSize = 16.sp,
                textAlign = TextAlign.Center,
                padding = PaddingValues(bottom = 4.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GetText(
                value = "$tempMax°",
                textSize = 20.sp,
                textAlign = TextAlign.Center
            )

            GetText(
                value = "max",
                textSize = 16.sp,
                textAlign = TextAlign.Center,
                padding = PaddingValues(bottom = 4.dp)
            )
        }
    }
}

fun getImageIcon(main: String?): Int {
    return when (main) {
        "Clouds" -> R.drawable.partlysunny
        "Rain" -> R.drawable.rain
        "Clear" -> R.drawable.clear
        else -> R.drawable.clear
    }
}

@Composable
fun ForeCastItem(weatherItem: ForeCastItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(3f),
            text = getDayOfWeek(weatherItem.dt_txt),
            style = TextStyle(
                color = Color.White,
                fontSize = 20.sp
            )
        )

        Image(
            modifier = Modifier
                .size(28.dp)
                .weight(1f),
            painter = painterResource(id = getImageIcon(weatherItem.weather?.get(0)?.main)),
            contentDescription = "cloudy"
        )

        Text(
            modifier = Modifier
            .weight(2f),
            text = "18°",
            textAlign = TextAlign.End,
            style = TextStyle(
                color = Color.White,
                fontSize = 20.sp
            )
        )

    }
}

fun getDayOfWeek(textDate: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = formatter.parse(textDate)
    return SimpleDateFormat("EEEE").format(date)
}

@Preview
@Composable
fun IndeterminateCircularIndicatorPreview() {
    IndeterminateCircularIndicator(true)
}

@Preview
@Composable
fun GetTopBoxPreview() {
    val weatherItem = MainWeatherItem(
        condition = "Cloudy",
        temp = "18",
        backgroundColor = R.color.cloudy,
        backgroundImage = R.drawable.forest_cloudy
    )
    GetTopBox(weatherItem,{/*Do nothing*/})
}


@Preview
@Composable
fun GetTempDistributionPreview() {
    GetTempDistribution(null)
}

@Preview
@Composable
fun ForeCastItemPreview() {
    val weatherItem = ForeCastItem(
        clouds = Clouds(0),
        dt = 1627777200L,
        dt_txt = "",
        main = Main(0.0, 0, 0, 0, 0, 0.0, 0.0,0.0, 0.0),
        pop = 0.0,
        rain = Rain(
            `3h` = 0.0,
            `1h`= 0.0
        ),
        sys = Sys(null, null, null, null, null, null),
        visibility = 0,
        weather = listOf(
            Weather(
                description = "Cloudy",
                icon = "",
                id = 0,
                main = ""
            )
        ),
        wind = Wind(0,0.0, 0.0)
    )
    ForeCastItem(weatherItem)
}

@Preview
@Composable
fun appBarPreview() {
    AppBar(
        onNavigationIconClick = {},
        icon = Icons.Default.ArrowBack,
        itemTitle = "Test Title",
        color = colorResource(id = R.color.cloudy)
    )
}
