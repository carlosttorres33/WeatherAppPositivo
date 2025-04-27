package com.carlostorres.weatherapppositivo.data.local

import android.util.Log
import com.carlostorres.weatherapppositivo.data.local.model.WeatherEntity
import javax.inject.Inject

class LocalWeatherDataSource @Inject  constructor(
    private val weatherDao: WeatherDao
){

    suspend fun insertWeatherInfo(weatherEntity: WeatherEntity) {
        Log.d("LocalWeatherDataSource", "Inserting weather info: $weatherEntity")
        weatherDao.upsertWeatherInfo(weatherEntity)
    }

    suspend fun getWeatherCitiesList(): List<WeatherEntity> {
        return weatherDao.getWeatherCitiesList()
    }

}