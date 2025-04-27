package com.carlostorres.weatherapppositivo.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel

@Entity(tableName = "weather_info_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = false)
    val id : Int,
    val name : String,
    val humidity: Int,
    val temp: Int,
    val windSpeed : Double,
    val rainPercentage : Int,
    val weatherDescription : String,
    val weatherMain : String,
    val coordLat : Double,
    val coordLon : Double
){
    fun toWeatherModel() = WeatherModel(
        id = this.id,
        name = this.name,
        humidity = this.humidity,
        temp = this.temp,
        windSpeed = this.windSpeed,
        rainPercentage = this.rainPercentage,
        weatherDescription = this.weatherDescription,
        weatherMain = this.weatherMain,
        coordLat = this.coordLat,
        coordLon = this.coordLon
    )
}
