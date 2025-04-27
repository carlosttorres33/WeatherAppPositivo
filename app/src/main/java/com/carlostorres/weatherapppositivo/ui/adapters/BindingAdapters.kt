package com.carlostorres.weatherapppositivo.ui.adapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.carlostorres.weatherapppositivo.R
import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel
import com.carlostorres.weatherapppositivo.utils.ConnectionStatus

@BindingAdapter("weatherIcon")
fun ImageView.setWeatherIcon(weather: WeatherModel?) {
    val resId = when (weather?.weatherMain ?: "Sun") {
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
fun TextView.setWeatherPlaceText(weather: WeatherModel?) {
    text = weather?.name ?: ""
}

@BindingAdapter(value = ["weather", "isLoading"], requireAll = true)
fun ConstraintLayout.showWeatherInfoCard(weather: WeatherModel?, isLoading: Boolean) {
    visibility = if (weather != null && !isLoading) View.VISIBLE else View.GONE
}


@BindingAdapter(value = ["weather", "isLoading"], requireAll = true)
fun TextView.showSearchTextMessage(weather: WeatherModel?, isLoading: Boolean){
    text = if (weather == null && !isLoading) context.getString(R.string.search_text) else ""
    visibility = if (weather == null && !isLoading) View.VISIBLE else View.GONE
}

@BindingAdapter("weatherTemperature")
fun TextView.weatherTemperature(weather: WeatherModel?){
    if (weather != null){
        text = "${weather.temp} °"
    }
}

@BindingAdapter("weatherDescription")
fun TextView.weatherDescription(weather: WeatherModel?){
    if (weather != null){
        text = "${weather.weatherDescription}"
    }
}

@BindingAdapter("rainPercentage")
fun TextView.rainPercentage(weather: WeatherModel?){
    text = if (weather != null){
        "${weather.rainPercentage} %"
    }else{
        "N/A"
    }
}

@BindingAdapter("windSpeed")
fun TextView.windSpeed(weather: WeatherModel?){
    text = if (weather != null){
        "${weather.windSpeed} Km/h"
    }else{
        "N/A"
    }
}

@BindingAdapter("humidity")
fun TextView.humidity(weather: WeatherModel?){
    text = if (weather != null){
        "${weather.humidity} %"
    }else{
        "N/A"
    }
}

@BindingAdapter("enableOfflineSearchClick")
fun View.enableOfflineSearchClick(hasInternet: ConnectionStatus){
    visibility = if (hasInternet == ConnectionStatus.Lost || hasInternet == ConnectionStatus.Unavailable) View.VISIBLE else View.GONE
}