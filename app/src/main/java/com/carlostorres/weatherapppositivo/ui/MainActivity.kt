package com.carlostorres.weatherapppositivo.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.carlostorres.weatherapppositivo.R
import com.carlostorres.weatherapppositivo.databinding.ActivityMainBinding
import com.carlostorres.weatherapppositivo.presentation.MainEvents
import com.carlostorres.weatherapppositivo.presentation.MainViewModel
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.Listener
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback, Listener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var googleMap: GoogleMap? = null
    private var easyWayLocation: EasyWayLocation? = null
    private var myLocationLatLng: LatLng? = null

    //Google Places
    private var places: PlacesClient? = null
    private var autoCompletePlace: AutocompleteSupportFragment? = null
    private var placeName = ""
    private var placeLatLng: LatLng? = null

    private var isLocationEnabled = false

    private val locationPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Log.i("Location", "Permiso concedido")
                easyWayLocation?.startLocation()
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                Log.i("Location", "Permiso concedido con limitación")
                easyWayLocation?.startLocation()
            }

            else -> {
                Log.i("Location", "Permiso denegado")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = Priority.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = 1f
        }
        easyWayLocation = EasyWayLocation(this, locationRequest, false, false, this)

        locationPermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        startGooglePlaces()

        initListeners()
        initObservers()

    }

    private fun initListeners() {
        binding.mbSearch.setOnClickListener {
            if (viewModel.state.value?.latitude != null && viewModel.state.value?.longitude != null) {
                viewModel.onEvent(MainEvents.OnSearchClicked)
            }
        }
    }

    private fun initObservers() {
        viewModel.state.observe(this) { state ->
            if (state.isLoading) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
            if (state.weather != null) {
                binding.tvWeatherInfo.text = state.weather.weather.first().description
                binding.tvWeatherPlace.text = placeName
                binding.tvTemperature.text = state.weather.main.temp.toString()
                binding.tvHumidity.text = state.weather.main.humidity.toString()
                binding.tvWind.text = state.weather.wind.speed.toString()
                binding.tvRain.text = state.weather.clouds.toString()
                binding.tvWeatherPlace.visibility = View.VISIBLE
                binding.tvTemperature.visibility = View.VISIBLE
                binding.tvHumidity.visibility = View.VISIBLE
                binding.tvWind.visibility = View.VISIBLE
                binding.tvRain.visibility = View.VISIBLE
                when(state.weather.weather.first().main){
                    "Clouds" -> binding.ivWeather.setImageResource(R.drawable.cloudy)
                    "Clear" -> binding.ivWeather.setImageResource(R.drawable.sunny)
                    "Rain" -> binding.ivWeather.setImageResource(R.drawable.rain)
                    "Snow" -> binding.ivWeather.setImageResource(R.drawable.snowy)
                    "Sun" -> binding.ivWeather.setImageResource(R.drawable.sunny)
                    else -> binding.ivWeather.setImageResource(R.drawable.wind)
                }
            }else{
                binding.tvWeatherPlace.visibility = View.GONE
                binding.tvTemperature.visibility = View.GONE
                binding.tvHumidity.visibility = View.GONE
                binding.tvWind.visibility = View.GONE
                binding.tvRain.visibility = View.GONE
            }
        }
    }

    private fun instanceAutoCompletePlace() {
        autoCompletePlace =
            supportFragmentManager.findFragmentById(R.id.fragmentPlace) as AutocompleteSupportFragment
        autoCompletePlace?.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
        )
        autoCompletePlace?.setHint("Buscar Lugar")
        autoCompletePlace?.setCountry("MX")
        autoCompletePlace?.setOnPlaceSelectedListener(
            object : PlaceSelectionListener {

                override fun onPlaceSelected(place: Place) {
                    placeName = place.name!!
                    placeLatLng = place.latLng!!
                    Log.i("Place", "Place: $placeName, $placeLatLng")
                    viewModel.onEvent(MainEvents.OnPlaceSelected(placeLatLng!!.latitude, placeLatLng!!.longitude))
                    googleMap?.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.builder().target(placeLatLng!!).zoom(17f).build()
                        )
                    )
                }

                override fun onError(p0: Status) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

    private fun limitSearch() {
        val northSide = SphericalUtil.computeOffset(myLocationLatLng, 5000.0, 0.0)
        val southSide = SphericalUtil.computeOffset(myLocationLatLng, 5000.0, 180.0)

        autoCompletePlace?.setLocationBias(RectangularBounds.newInstance(southSide, northSide))
    }

    private fun startGooglePlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }

        places = Places.createClient(this)
        instanceAutoCompletePlace()

    }

    override fun onMapReady(map: GoogleMap) {

        googleMap = map

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleMap?.isMyLocationEnabled = true

    }

    override fun onResume() {
        super.onResume()
        easyWayLocation?.startLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        easyWayLocation?.endUpdates()
    }

    override fun locationOn() {

    }

    override fun currentLocation(location: Location) {
        if (placeLatLng == null) {

            myLocationLatLng = LatLng(location.latitude, location.longitude)

            googleMap?.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.builder().target(myLocationLatLng!!).zoom(17f).build()
                )
            )
        }

        if (!isLocationEnabled) {
            isLocationEnabled = true
            limitSearch()
        }
    }

    override fun locationCancelled() {

    }


}