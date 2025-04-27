package com.carlostorres.weatherapppositivo.data.remote.model

import com.carlostorres.weatherapppositivo.data.local.model.WeatherEntity
import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel

data class WeatherResponseDto(
    val clouds: Clouds,
    val id: Int,
    val main: Main,
    val name: String,
    val weather: List<Weather>,
    val wind: Wind,
    val coord: Coord
) {

    fun toWeatherModel() = WeatherModel(
        id = this.id,
        name = this.name,
        humidity = this.main.humidity,
        temp = this.main.temp.toString().substringBefore(".").toInt(),
        windSpeed = this.wind.speed,
        rainPercentage = this.clouds.all,
        weatherDescription = this.weather.first().description,
        weatherMain = this.weather.first().main,
        coordLat = this.coord.lat,
        coordLon = this.coord.lon
    )

    fun toWeatherEntity() = WeatherEntity(
        id = this.id,
        name = this.name,
        humidity = this.main.humidity,
        temp = this.main.temp
            .toString()
            .substringBefore(".")
            .toInt(),
        windSpeed = this.wind.speed,
        rainPercentage = this.clouds.all,
        weatherDescription = this.weather.first().description,
        weatherMain = this.weather.first().main,
        coordLat = this.coord.lat,
        coordLon = this.coord.lon
    )

}