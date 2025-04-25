package com.carlostorres.weatherapppositivo.presentation

import com.carlostorres.weatherapppositivo.data.remote.model.WeatherResponse

data class MainState(
    val isLoading: Boolean = false,
    val weather: WeatherResponse? = null,
    val error: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
