package com.carlostorres.weatherapppositivo.data.repository

import android.util.Log
import com.carlostorres.weatherapppositivo.data.remote.RemoteWeatherDataSource
import com.carlostorres.weatherapppositivo.data.remote.model.WeatherResponse
import com.carlostorres.weatherapppositivo.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImplementation @Inject constructor(
    private val remoteWeatherDataSource: RemoteWeatherDataSource
) : WeatherRepository {

    override suspend fun getWeatherFromCoordinates(
        latitude: Double,
        longitude: Double
    ): WeatherResponse? {
        return try {
            val response = remoteWeatherDataSource.getWeatherFromCoordinates(latitude, longitude)
            Log.d("WeatherRepositoryImpl", "Response: ${response.body()}")
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("WeatherRepositoryImpl", "Error fetching weather data: ${e.message}")
            null
        }
    }

}