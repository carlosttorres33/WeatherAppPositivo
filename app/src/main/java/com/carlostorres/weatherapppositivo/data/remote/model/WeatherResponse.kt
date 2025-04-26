package com.carlostorres.weatherapppositivo.data.remote.model

data class WeatherResponse(
    val clouds: Clouds,
    val id: Int,
    val main: Main,
    val name: String,
    val weather: List<Weather>,
    val wind: Wind
)