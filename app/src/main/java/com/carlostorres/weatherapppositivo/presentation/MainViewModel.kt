package com.carlostorres.weatherapppositivo.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.weatherapppositivo.domain.usecases.GetCitiesWeatherSavedUseCase
import com.carlostorres.weatherapppositivo.domain.usecases.GetWeatherFromCoordinatesUseCase
import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel
import com.carlostorres.weatherapppositivo.utils.ConnectionStatus
import com.carlostorres.weatherapppositivo.utils.ConnectivityObserver
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeatherFromCoordinatesUseCase: GetWeatherFromCoordinatesUseCase,
    private val getCitiesWeatherSavedUseCase: GetCitiesWeatherSavedUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _isConnected =  connectivityObserver.isConnected.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        ConnectionStatus.Available
    )
    val isConnected : StateFlow<ConnectionStatus> = _isConnected

    private val _isLoading = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = _isLoading
    private val _weather = MutableLiveData<WeatherModel?>()
    val weather : LiveData<WeatherModel?> = _weather
    private val _error = MutableLiveData<String?>()
    val error : LiveData<String?> = _error

    private val _citiesWeather = MutableStateFlow<List<WeatherModel>>(emptyList())
    val citiesWeather : StateFlow<List<WeatherModel>> = _citiesWeather

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

            MainEvents.GetCitiesWeatherSaved -> {
                getCitiesWeatherSaved()
            }

            is MainEvents.OfflineCitySelected -> {
                event.setSearchText()
                Log.d("MainViewModel", event.weather.name)
                _weather.value = event.weather
            }

        }
    }

    private fun getCitiesWeatherSaved() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val citiesWeatherList = getCitiesWeatherSavedUseCase()
            _citiesWeather.value = citiesWeatherList
        }catch (e:Exception){
            e.printStackTrace()
            _citiesWeather.value = emptyList()
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
                updateState(
                    isLoading = false,
                    weather = weatherResponse,
                    error = null
                )
                Log.d("MainViewModel", "Weather data fetched successfully")
            } else {
                updateState(
                    isLoading = false,
                    weather = null,
                    error = "Error fetching weather data"
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MainViewModel", "Error fetching weather data: ${e.message}")
            updateState(
                isLoading = false,
                weather = null,
                error = e.message
            )
        }

    }

    private fun updateState(
        isLoading: Boolean,
        weather: WeatherModel?,
        error: String?
    ){

        _isLoading.postValue(isLoading)
        _weather.postValue(weather)
        _error.postValue(error)

    }

}