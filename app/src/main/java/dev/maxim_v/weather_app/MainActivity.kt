package dev.maxim_v.weather_app

import android.location.Geocoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.maxim_v.weather_app.domain.entity.WeatherType
import dev.maxim_v.weather_app.ui.theme.WeatherForecastTheme
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val geocoder = Geocoder(applicationContext)
        val s = "2024-07-04T00:00"
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").parse(s)
        val d = DateTimeFormatter.ofPattern("d MMMM yyyy, hh:mm").format(dateFormat)
        setContent {
            WeatherForecastTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        weather = Weather(
                            city = geocoder.getFromLocationName("Tyumen", 1)!!.first().latitude.toString(),
                            temp = "30°C",
                            date = d,
                            weather = WeatherType.CLEAR
                        ),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(weather: Weather, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = weather.city)
                Text(text = weather.date)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        fontSize = 48.sp,
                        text = weather.temp
                    )
                    Image(
                        painter = painterResource(id = selectIcon(weather.weather)),
                        contentDescription = null
                    )
                }

            }
        }
    }
}

fun selectIcon(weatherType: WeatherType): Int {
    return when (weatherType) {
        WeatherType.CLEAR -> R.drawable.sunny
        WeatherType.SNOW -> R.drawable.snowy
        WeatherType.RAIN -> R.drawable.rainy
        WeatherType.THUNDER -> R.drawable.rainthunder
        WeatherType.CLOUDY -> R.drawable.partlycloudy
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherForecastTheme {
        Greeting(
            weather = Weather(
                city = "Tyumen",
                temp = "30°C",
                date = "Thursday 4, July, 16:00",
                weather = WeatherType.CLEAR
            )
        )
    }
}

data class Weather(
    val city: String,
    val temp: String,
    val date: String,
    val weather: WeatherType
)
