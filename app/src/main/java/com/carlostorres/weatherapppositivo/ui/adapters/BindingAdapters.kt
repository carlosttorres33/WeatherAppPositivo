package com.carlostorres.weatherapppositivo.ui.adapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.carlostorres.weatherapppositivo.R
import com.carlostorres.weatherapppositivo.data.remote.model.WeatherResponse

@BindingAdapter("weatherIcon")
fun ImageView.setWeatherIcon(weather: WeatherResponse?) {
    val resId = when (weather?.weather?.first()?.main ?: "Sun") {
        "Clouds" -> R.drawable.cloudy
        "Clear" -> R.drawable.cloudy_sunny
        "Rain" -> R.drawable.rain
        "Snow" -> R.drawable.snowy
        "Sun" -> R.drawable.sunny
        else -> R.drawable.wind
    }
    setImageResource(resId)
}

@BindingAdapter("isVisible")
fun View.setIsVisible(visible: Boolean) {
    Log.d("BindingAdapter", "setIsVisible called with: $visible")
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("setWeatherPlaceText")
fun TextView.setWeatherPlaceText(weather: WeatherResponse?) {
    text = weather?.name ?: ""
}

@BindingAdapter(value = ["weather", "isLoading"], requireAll = true)
fun ConstraintLayout.showWeatherInfoCard(weather: WeatherResponse?, isLoading: Boolean) {
    visibility = if (weather != null && !isLoading) View.VISIBLE else View.GONE
}


@BindingAdapter(value = ["weather", "isLoading"], requireAll = true)
fun TextView.showSearchTextMessage(weather: WeatherResponse?, isLoading: Boolean){
    text = if (weather == null && !isLoading) context.getString(R.string.search_text) else ""
    visibility = if (weather == null && !isLoading) View.VISIBLE else View.GONE
}

@BindingAdapter("weatherTemperature")
fun TextView.weatherTemperature(weather: WeatherResponse?){
    if (weather != null){
        text = "${weather.main.temp.toString().substringBefore(".")}°"
    }
}

@BindingAdapter("weatherDescription")
fun TextView.weatherDescription(weather: WeatherResponse?){
    if (weather != null){
        text = "${weather.weather.first().description}"
    }
}

@BindingAdapter("rainPercentage")
fun TextView.rainPercentage(weather: WeatherResponse?){
    text = if (weather != null){
        "${weather.clouds.all} %"
    }else{
        "N/A"
    }
}

@BindingAdapter("windSpeed")
fun TextView.windSpeed(weather: WeatherResponse?){
    text = if (weather != null){
        "${weather.wind.speed} Km/h"
    }else{
        "N/A"
    }
}

@BindingAdapter("humidity")
fun TextView.humidity(weather: WeatherResponse?){
    text = if (weather != null){
        "${weather.main.humidity} %"
    }else{
        "N/A"
    }
}