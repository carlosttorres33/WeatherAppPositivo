package com.carlostorres.weatherapppositivo.presentation

import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel
import com.google.android.gms.maps.model.LatLng

sealed interface MainEvents {
    data object OnSearchPlaceWeather : MainEvents
    data object GetMyCurrentLocationWeather : MainEvents
    data class ChangeLocation(val newLocation: LatLng) : MainEvents
    data object GetCitiesWeatherSaved: MainEvents
    data class OfflineCitySelected (val weather : WeatherModel, val setSearchText: () -> Unit) : MainEvents

}