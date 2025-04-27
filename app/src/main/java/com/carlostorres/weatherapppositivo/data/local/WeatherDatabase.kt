package com.carlostorres.weatherapppositivo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carlostorres.weatherapppositivo.data.local.model.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

}