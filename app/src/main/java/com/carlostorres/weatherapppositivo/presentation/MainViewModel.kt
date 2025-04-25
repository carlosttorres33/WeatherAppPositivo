package com.carlostorres.weatherapppositivo.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.weatherapppositivo.data.remote.model.WeatherResponse
import com.carlostorres.weatherapppositivo.domain.usecases.GetWeatherFromCoordinatesUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeatherFromCoordinatesUseCase: GetWeatherFromCoordinatesUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = _isLoading
    private val _weather = MutableLiveData<WeatherResponse?>()
    val weather : LiveData<WeatherResponse?> = _weather
    private val _error = MutableLiveData<String?>()
    val error : LiveData<String?> = _error

    private val _locationLatLng = MutableLiveData<LatLng?>(null)
    val locationLatLng : LiveData<LatLng?> = _locationLatLng

    fun onEvent(event: MainEvents) {
        when (event) {
            is MainEvents.OnSearchPlaceWeather -> {
                if (_locationLatLng.value?.latitude != null || _locationLatLng.value?.longitude != null) {
                    getWeatherFromCoordinates()
                }
            }
            is MainEvents.GetMyCurrentLocationWeather -> {
                getWeatherFromCoordinates()
            }
            is MainEvents.ChangeLocation -> {
                _locationLatLng.value = LatLng(event.newLocation.latitude, event.newLocation.longitude)
                println("NewLocation: ${_locationLatLng.value}, event: ${event.newLocation}")
            }
        }
    }

    private fun getWeatherFromCoordinates() = viewModelScope.launch(Dispatchers.IO) {

        _isLoading.postValue(true)

        try {

            val weatherResponse = getWeatherFromCoordinatesUseCase(
                latitude = _locationLatLng.value!!.latitude,
                longitude = _locationLatLng.value!!.longitude
            )

            if (weatherResponse != null) {
                _isLoading.postValue(false)
                _weather.postValue(weatherResponse)
                _error.postValue(null)
                Log.d("MainViewModel", "Weather data fetched successfully")
            } else {
                _isLoading.postValue(false)
                _weather.postValue(null)
                _error.postValue("Error fetching weather data")
                Log.e("MainViewModel", "Error fetching weather data")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MainViewModel", "Error fetching weather data: ${e.message}")
            _isLoading.postValue(false)
            _weather.postValue(null)
            _error.postValue(e.message)
        }

    }

}