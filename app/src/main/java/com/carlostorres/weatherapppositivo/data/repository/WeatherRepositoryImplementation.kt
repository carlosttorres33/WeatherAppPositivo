package com.carlostorres.weatherapppositivo.data.repository

import android.util.Log
import com.carlostorres.weatherapppositivo.data.remote.RemoteWeatherDataSource
import com.carlostorres.weatherapppositivo.data.remote.model.WeatherResponseDto
import com.carlostorres.weatherapppositivo.domain.repository.WeatherRepository
import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel
import javax.inject.Inject

class WeatherRepositoryImplementation @Inject constructor(
    private val remoteWeatherDataSource: RemoteWeatherDataSource
) : WeatherRepository {

    override suspend fun getWeatherFromCoordinates(
        latitude: Double,
        longitude: Double
    ): WeatherModel? {
        return try {
            val response = remoteWeatherDataSource.getWeatherFromCoordinates(latitude, longitude)
            Log.d("WeatherRepositoryImpl", "Response: ${response.body()}")
            if (response.isSuccessful && response.body() != null) {
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

}