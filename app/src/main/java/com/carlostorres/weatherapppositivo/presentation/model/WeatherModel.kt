package com.carlostorres.weatherapppositivo.presentation.model

data class WeatherModel(
    val id : Int,
    val name : String,
    val humidity: Int,
    val temp: Int,
    val windSpeed : Double,
    val rainPercentage : Int,
    val weatherDescription : String,
    val weatherMain : String
)