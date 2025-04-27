package com.carlostorres.weatherapppositivo.ui.components.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.carlostorres.weatherapppositivo.R
import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel

@Composable
fun LocalPlaceItem(
    modifier: Modifier = Modifier,
    place: WeatherModel,
    onItemClickListener: (WeatherModel) -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .height(30.dp)
            .clickable {
                onItemClickListener(place)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = place.name,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
            painter = painterResource(id = setWeatherIcon(place.weatherMain)),
            contentDescription = "Drawable",
            contentScale = ContentScale.Crop
        )

    }

}

fun setWeatherIcon(whetherMain: String): Int{

    return when(whetherMain){
        "Clouds" -> R.drawable.cloudy
        "Clear" -> R.drawable.cloudy_sunny
        "Rain" -> R.drawable.rain
        "Snow" -> R.drawable.snowy
        "Sun" -> R.drawable.sunny
        else -> R.drawable.wind
    }

}