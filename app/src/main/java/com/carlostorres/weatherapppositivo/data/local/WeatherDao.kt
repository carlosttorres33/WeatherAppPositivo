package com.carlostorres.weatherapppositivo.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.carlostorres.weatherapppositivo.data.local.model.WeatherEntity

@Dao
interface WeatherDao {

    @Upsert
    fun upsertWeatherInfo(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weather_info_table")
    fun getWeatherCitiesList(): List<WeatherEntity>

}