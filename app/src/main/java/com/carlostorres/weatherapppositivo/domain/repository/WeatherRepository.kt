package com.carlostorres.weatherapppositivo.domain.repository

import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel

interface WeatherRepository {

    suspend fun getWeatherFromCoordinates(latitude: Double, longitude: Double) : WeatherModel?

    suspend fun getCitiesWeatherSaved() : List<WeatherModel>

}
