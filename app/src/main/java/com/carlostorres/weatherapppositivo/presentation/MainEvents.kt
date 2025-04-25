package com.carlostorres.weatherapppositivo.presentation

sealed interface MainEvents {
    data object OnSearchClicked : MainEvents
    data class OnPlaceSelected(val latitude: Double, val longitude: Double) : MainEvents
}