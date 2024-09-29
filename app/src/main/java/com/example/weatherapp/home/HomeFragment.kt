package com.example.weatherapp.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.AppDatabase
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.network.API
import com.example.weatherapp.network.ForecastState
import com.example.weatherapp.network.WeatherState
import com.example.weatherapp.util.WeatherViewModelFactory
import com.example.weatherapp.util.isNetworkAvailable
import com.example.weatherapp.util.toAMPM
import com.example.weatherapp.util.toDaysTime
import com.example.weatherapp.util.toDrawable
import com.example.weatherapp.util.toFahrenheit
import com.example.weatherapp.util.toKelvin
import com.example.weatherapp.util.toMilesPerHour
import com.example.weatherapp.util.toTwoDecimalPlaces
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch

const val REQUEST_LOCATION_CODE = 2005


class HomeFragment : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentHomeBinding
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private val viewModel: HomeFragmentViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(
                    AppDatabase.getInstance(requireContext()).weatherDao(),
                    AppDatabase.getInstance(requireContext()).alarmDao()
                ),
                WeatherRemoteDataSource.getInstance(API.retrofitService),
                WeatherSharedPreferenceDataSource.getInstance(this.requireContext())
            )
        )
        ViewModelProvider(this, factory)[HomeFragmentViewModel::class.java]
    }
    private var tempUnit: String = ""
    private var speedUnit: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnAllowPerm.setOnClickListener {
            requestPerms()
        }
        binding.btnEnableLocation.setOnClickListener {
            enableLocationServices()
        }
        binding.btnAllowNetwork.setOnClickListener {
            enableInternet()
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.postDelayed({
                refreshData()
            }, 1000)
        }

    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.Home)
        if (viewModel.getFirstTimeData()) {
            // handle network
            if (isNetworkAvailable(this.requireContext())) {
                binding.cvNetwork.visibility = View.GONE
                // is it gps
                if (viewModel.getLocationSettings() == "GPS") {
                    initPermissions()
                    launchStateFlows()
                }
                // map
                else {
                    latitude = viewModel.getLatitude()
                    longitude = viewModel.getLongitude()
                    viewModel.getWeatherData(latitude, longitude)
                    viewModel.getForecastWeatherData(latitude, longitude)
                    launchStateFlows()
                }
            }
        }
        // not first time
        else {
            binding.cvNetwork.visibility = View.GONE
            viewModel.getWeatherDataLocal()
            viewModel.getForecastDataLocal()
            launchStateFlows()
        }


    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    private fun launchStateFlows() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentWeather.collect {
                    tempUnit = viewModel.getUnitTemp()
                    speedUnit = viewModel.getWindSpeedUnit()
                    when (it) {
                        is WeatherState.Error -> {
                            Log.i("here", "onViewCreated: Weather State " + it.message)
                        }

                        is WeatherState.Loading -> {
                            binding.pb.visibility = View.VISIBLE
                        }

                        is WeatherState.Success -> {
                            binding.pb.visibility = View.GONE
                            binding.clHomeFragment.visibility = View.VISIBLE
                            binding.tvDateAndTime.text =
                                it.weatherData.dt.toDaysTime() + " " + it.weatherData.dt.toAMPM()
                            binding.tvCityName.text = it.weatherData.name
                            when (tempUnit) {
                                "C" -> binding.tvTemp.text =
                                    it.weatherData.main.temp.toTwoDecimalPlaces()
                                        .toString() + " $tempUnit" + "°"

                                "K" -> binding.tvTemp.text =
                                    it.weatherData.main.temp.toKelvin().toTwoDecimalPlaces()
                                        .toString() + " $tempUnit" + "°"

                                "F" -> binding.tvTemp.text =
                                    it.weatherData.main.temp.toFahrenheit().toTwoDecimalPlaces()
                                        .toString() + " $tempUnit" + "°"
                            }
                            binding.tvDescription.text =
                                it.weatherData.weather[0].description.replaceFirstChar { it.uppercaseChar() }
                            binding.ivImage.setImageResource(it.weatherData.weather[0].icon.toDrawable())
                            when (speedUnit) {
                                "ms" -> binding.tvWindSpeed.text =
                                    it.weatherData.wind.speed.toString() + " Meter/Sec"

                                "mh" -> binding.tvWindSpeed.text =
                                    it.weatherData.wind.speed.toMilesPerHour().toTwoDecimalPlaces()
                                        .toString() + " Mile/Hour"

                            }
                            binding.tvHumidity.text = it.weatherData.main.humidity.toString() + " %"
                            binding.tvPressure.text =
                                it.weatherData.main.pressure.toString() + " hpa"
                            binding.tvCloud.text = it.weatherData.clouds.all.toString() + " %"
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentForecastWeather.collect {
                    tempUnit = viewModel.getUnitTemp()
                    speedUnit = viewModel.getWindSpeedUnit()
                    when (it) {
                        is ForecastState.Error -> {
                            Log.i("here", "onViewCreated: forecast " + it.message)
                        }

                        is ForecastState.Loading -> {
                            Log.i("here", "onViewCreated: " + it.toString())
                        }

                        is ForecastState.Success -> {
                            val managerHourly = LinearLayoutManager(requireContext())
                            managerHourly.setOrientation(RecyclerView.HORIZONTAL)
                            val managerDaily = LinearLayoutManager(requireContext())
                            managerDaily.setOrientation(RecyclerView.VERTICAL)
                            val tempUnit: String = viewModel.getUnitTemp()
                            binding.rvHourlyForecast.layoutManager = managerHourly
                            binding.rvHourlyForecast.adapter = HourlyRecyclerViewAdapter(
                                it.forecastData.list.take(8),
                                tempUnit
                            )

                            binding.rvDailyForecast.layoutManager = managerDaily
                            binding.rvDailyForecast.adapter = DailyRecyclerViewAdapter(
                                it.forecastData.list.filterIndexed { index, _ -> index == 0 || (index + 1) % 8 == 0 },
                                tempUnit
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestPerms() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                requireContext(),
                "Please allow permission manually",
                Toast.LENGTH_SHORT
            ).show()
        }
        requestPermissions(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_LOCATION_CODE
        )
    }

    private fun initPermissions(ref: Boolean = false) {
        if (checkPermissions()) {
            binding.cvPermissions.visibility = View.GONE
            binding.cvLocation.visibility = View.VISIBLE
            if (isLocationEnabled()) {
                binding.cvLocation.visibility = View.GONE
                getFreshLocation()
                viewModel.setLongitude(longitude)
                viewModel.setLatitude(latitude)
                if (ref) {
                    viewModel.refreshData(latitude, longitude)
                } else {
                    viewModel.getWeatherData(latitude, longitude)
                    viewModel.getForecastWeatherData(latitude, longitude)
                }


            }
        } else {


            binding.cvPermissions.visibility = View.VISIBLE
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_CODE
            )
        }
    }

    private fun checkPermissions(): Boolean {
        return requireContext().checkSelfPermission(
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                &&
                requireContext().checkSelfPermission(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun enableLocationServices() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun enableInternet() {
        val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
        startActivity(intent)
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                binding.cvPermissions.visibility = View.GONE
                binding.cvLocation.visibility = View.VISIBLE
                if (isLocationEnabled()) {
                    binding.cvLocation.visibility = View.GONE
                    binding.cvLocation.visibility = View.GONE
                    getFreshLocation()
                    viewModel.setLongitude(longitude)
                    viewModel.setLatitude(latitude)
                    viewModel.getWeatherData(latitude, longitude)
                    viewModel.getForecastWeatherData(latitude, longitude)
                    launchStateFlows()
                }
            }
        }

    }


    @SuppressLint("MissingPermission")
    fun getFreshLocation() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply { setPriority(Priority.PRIORITY_HIGH_ACCURACY) }
                .build(),
            object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    fusedLocationProviderClient.removeLocationUpdates(this)
                    longitude = p0.locations[0].longitude
                    latitude = p0.locations[0].latitude
                    viewModel.setLatitude(latitude)
                    viewModel.setLongitude(longitude)
                }
            }, Looper.myLooper()
        )
    }

    private fun refreshData() {


        if (isNetworkAvailable(this.requireContext())) {
            binding.clHomeFragment.visibility = View.GONE
            binding.pb.visibility = View.VISIBLE
            binding.cvNetwork.visibility = View.GONE
            // is it gps
            if (viewModel.getLocationSettings() == "GPS") {
                initPermissions(true)
            }
            // map
            else {
                latitude = viewModel.getLatitude()
                longitude = viewModel.getLongitude()
                viewModel.refreshData(latitude, longitude)

            }
        }
        else{
            Toast.makeText(this.requireContext(), "Please enable internet to refresh", Toast.LENGTH_SHORT).show()
        }

        binding.swipeRefresh.isRefreshing = false
    }
}