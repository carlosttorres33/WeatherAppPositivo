package com.carlostorres.weatherapppositivo.data.remote

import android.content.Context
import com.carlostorres.weatherapppositivo.R
import com.carlostorres.weatherapppositivo.data.remote.model.WeatherResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

class RemoteWeatherDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val weatherService: WeatherService
) {

    suspend fun getWeatherFromCoordinates(
        latitude: Double,
        longitude: Double
    ): Response<WeatherResponse> {
        val weatherFromCoordinates = weatherService.getWeatherFromCoordinates(
            latitude = latitude,
            longitude = longitude,
            apiKey = context.getString(R.string.api_key)
        )
        return weatherFromCoordinates
    }

}
