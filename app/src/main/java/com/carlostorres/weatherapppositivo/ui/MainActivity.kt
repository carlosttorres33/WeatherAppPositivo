package com.carlostorres.weatherapppositivo.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.carlostorres.weatherapppositivo.R
import com.carlostorres.weatherapppositivo.databinding.ActivityMainBinding
import com.carlostorres.weatherapppositivo.presentation.MainEvents
import com.carlostorres.weatherapppositivo.presentation.MainViewModel
import com.carlostorres.weatherapppositivo.utils.ConnectionStatus
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.Listener
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
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
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback, Listener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var googleMap: GoogleMap? = null
    private var easyWayLocation: EasyWayLocation? = null

    //Google Places
    private var places: PlacesClient? = null
    private var autoCompletePlace: AutocompleteSupportFragment? = null
    private var placeName = ""

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
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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
        initObservers()

    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isConnected.collect { connectionStatus ->
                    if (connectionStatus is ConnectionStatus.Lost) {
                            Snackbar.make(binding.root, "No tienes conexión", Snackbar.LENGTH_LONG)
                                .setAction("Ocultar"){}
                                .show()
                    }
                }
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
                    viewModel.onEvent(
                        event = MainEvents.ChangeLocation(
                            newLocation = place.latLng!!
                        )
                    )
                    Log.i("Place", "Place: $placeName, ${viewModel.locationLatLng}")
                    googleMap?.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.builder().target(viewModel.locationLatLng.value!!)
                                .zoom(15f).build()
                        )
                    )
                    viewModel.onEvent(
                        event = MainEvents.OnSearchPlaceWeather
                    )
                }

                override fun onError(p0: Status) {
                    println("Error: ${p0.statusMessage}")
                }

            }
        )
    }

    private fun limitSearch() {
        val northSide = SphericalUtil.computeOffset(
            viewModel.locationLatLng?.value ?: LatLng(0.0, 0.0),
            1000.0,
            0.0
        )
        val southSide = SphericalUtil.computeOffset(
            viewModel.locationLatLng?.value ?: LatLng(0.0, 0.0),
            1000.0,
            180.0
        )

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

        if (viewModel.locationLatLng.value == null) {

            val myLocationLatLng = LatLng(location.latitude, location.longitude)

            viewModel.onEvent(
                MainEvents.ChangeLocation(
                    newLocation = myLocationLatLng
                )
            )

            println("User Location tracked")

            viewModel.onEvent(
                MainEvents.GetMyCurrentLocationWeather
            )

            googleMap?.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.builder().target(myLocationLatLng).zoom(15f).build()
                )
            )
            easyWayLocation?.endUpdates()
        }

        if (!isLocationEnabled) {
            isLocationEnabled = true
            limitSearch()
        }
    }

    override fun locationCancelled() {

    }


}