package com.carlostorres.weatherapppositivo.presentation

import com.google.android.gms.maps.model.LatLng

sealed interface MainEvents {
    data object OnSearchPlaceWeather : MainEvents
    data object GetMyCurrentLocationWeather : MainEvents
    data class ChangeLocation(val newLocation: LatLng) : MainEvents
}