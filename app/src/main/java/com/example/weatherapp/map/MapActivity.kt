package com.example.weatherapp.map

import android.content.Intent
import android.graphics.Rect
import android.location.Geocoder
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.AppDatabase
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.databinding.ActivityMapBinding
import com.example.weatherapp.network.API
import com.example.weatherapp.util.WeatherViewModelFactory
import com.example.weatherapp.util.cities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.util.Locale

class MapActivity : AppCompatActivity(), OnSearchItemClick, OnClickUpdateHomeLocation,
    OnClickAddToFav {
    private lateinit var binding: ActivityMapBinding
    private lateinit var map: MapView
    private lateinit var adapter: SearchRecyclerViewAdapter
    private lateinit var sharedFlow: MutableSharedFlow<String>
    private lateinit var intent: Intent
    private var isFav: Boolean = false
    private var long: Double? = null
    private var lat: Double? = null
    private var city: String? = null
    private val viewModel: MapActivityViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(AppDatabase.getInstance(this).weatherDao()),
                WeatherRemoteDataSource.getInstance(API.retrofitService),
                WeatherSharedPreferenceDataSource.getInstance(this)
            )
        )
        ViewModelProvider(this, factory)[MapActivityViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent = getIntent()
        isFav = intent.getBooleanExtra("fav", false)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        map = binding.mapView
        setupMap()

        adapter = SearchRecyclerViewAdapter(listOf(), this)
        binding.rvSearch.adapter = adapter
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        sharedFlow = MutableSharedFlow()
        binding.searchView.addTextChangedListener {
            lifecycleScope.launch {
                sharedFlow.emit(it.toString())
            }
        }

        lifecycleScope.launch {
            sharedFlow.collect {
                adapter.setData(cities.filter { name -> name.contains(it, true) })
            }
        }

        binding.searchView.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                getLatLongFromCity(binding.searchView.text.toString())
            }
            true
        }
    }


    private fun setupMap() {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.mapCenter
        map.getLocalVisibleRect(Rect())
        map.setMultiTouchControls(true)
        map.controller.setZoom(10.0)
        map.controller.setCenter(GeoPoint(31.0, 29.0))
        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(point: GeoPoint?): Boolean {
                point?.let {
                    lat = it.latitude
                    long = it.longitude
                    val marker = Marker(map)
                    marker.position = GeoPoint(lat!!, long!!)
                    map.overlays.add(marker)
                    map.controller.setCenter(GeoPoint(lat!!, long!!))
                    map.controller.setZoom(13.0)
                    if (lat != null) GlobalScope.launch(Dispatchers.IO) {
                        launch(Dispatchers.IO) {
                            getCityFromLatLong(lat!!, long!!)
                        }.join()
                        launch(Dispatchers.Main) {
                            val dialog = MapDialog(
                                this@MapActivity, isFav, this@MapActivity, this@MapActivity
                            )
                            if (city != null) {
                                dialog.show()
                                dialog.showInfoForMap(lat!!, long!!, city!!)
                            }
                        }
                    }
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }
        val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
        map.overlays.add(mapEventsOverlay)
    }

    private fun showOnMap(lat: Double, lon: Double) {
        val marker = Marker(map)
        marker.position = GeoPoint(lat, lon)
        map.overlays.add(marker)
        map.invalidate()
        map.controller.setCenter(GeoPoint(lat, lon))
    }

    private fun getLatLongFromCity(cityName: String) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocationName(cityName, 1)
            if (!addresses.isNullOrEmpty()) {
                lat = addresses[0].latitude
                long = addresses[0].longitude
                if (addresses[0].adminArea != null) city =
                    (addresses[0].adminArea + " - " + addresses[0].countryCode)
                else city = cityName.replaceFirstChar { it.uppercaseChar() }
                showOnMap(lat!!, long!!)
                val dialog =
                    MapDialog(this@MapActivity, isFav, this@MapActivity, this@MapActivity)
                if (city != null && lat != null) {
                    dialog.show()
                    dialog.showInfoForMap(lat!!, long!!, city!!)
                }
            } else {
                Toast.makeText(this, "City Not Found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCityFromLatLong(lat: Double, lon: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lon, lat, 1)
            if (addresses!!.get(0).adminArea !=null) {
                city = addresses[0].adminArea + " " + addresses[0].countryCode
            } else city = "Unknown City"
        } catch (e: Exception) {
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun locationLookUp(item: String) {
        city = item
        adapter.setData(cities.filter { false })
        getLatLongFromCity(item.substringBefore(" - "))
    }

    override fun addToFav() {
        viewModel.addToFav(city!!, lat!!, long!!)
    }

    override fun updateHomeLocation() {
        TODO("Not yet implemented")
    }
}