package com.carlostorres.weatherapppositivo.data.repository

import android.util.Log
import com.carlostorres.weatherapppositivo.data.local.LocalWeatherDataSource
import com.carlostorres.weatherapppositivo.data.remote.RemoteWeatherDataSource
import com.carlostorres.weatherapppositivo.domain.repository.WeatherRepository
import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel
import javax.inject.Inject

class WeatherRepositoryImplementation @Inject constructor(
    private val remoteWeatherDataSource: RemoteWeatherDataSource,
    private val localWeatherDataSource: LocalWeatherDataSource
) : WeatherRepository {

    override suspend fun getWeatherFromCoordinates(
        latitude: Double,
        longitude: Double
    ): WeatherModel? {
        return try {
            val response = remoteWeatherDataSource.getWeatherFromCoordinates(latitude, longitude)
            Log.d("WeatherRepositoryImpl", "Response: ${response.body()}")
            if (response.isSuccessful && response.body() != null) {
                localWeatherDataSource.insertWeatherInfo(response.body()!!.toWeatherEntity())
                response.body()!!.toWeatherModel()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("WeatherRepositoryImpl", "Error fetching weather data: ${e.message}")
            null
        }
    }

    override suspend fun getCitiesWeatherSaved(): List<WeatherModel> {
        return try {
            val citiesList = localWeatherDataSource.getWeatherCitiesList()
            citiesList.map {
                it.toWeatherModel()
            }
        }catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
    }

}