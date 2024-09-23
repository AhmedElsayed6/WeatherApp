package com.example.weatherapp.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
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
import com.example.weatherapp.data.source.WeatherData
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.network.API
import com.example.weatherapp.network.ForecastState
import com.example.weatherapp.network.WeatherState
import com.example.weatherapp.util.WeatherViewModelFactory
import com.example.weatherapp.util.toDaysTime
import com.example.weatherapp.util.toDrawable
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch

const val REQUEST_LOCATION_CODE = 2005

class HomeFragment : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val viewModel: HomeFragmentViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource(),
                WeatherRemoteDataSource.getInstance(API.retrofitService),
                WeatherSharedPreferenceDataSource.getInstance(this.requireContext())
            )
        )
        ViewModelProvider(this, factory)[HomeFragmentViewModel::class.java]
    }
    private lateinit var binding: FragmentHomeBinding


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

    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.title = "Home"
        if (viewModel.getLocationSettings() == "GPS") {
            initPermissions()
        } else {

        }
    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    private fun launchStateFlows() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentWeather.collect {
                    when (it) {
                        is WeatherState.Error -> {
                            Log.i("here", "onViewCreated: " + it.message)
                        }

                        is WeatherState.Loading -> {
                            binding.pb.visibility = View.VISIBLE
                        }

                        is WeatherState.Success -> {
                            binding.pb.visibility = View.GONE
                            binding.tvCityName.text = it.weatherData.name
                            binding.tvTemp.text =
                                it.weatherData.main.temp.toString() + " ${viewModel.getUnitTemp()}"
                            binding.tvDescription.text = it.weatherData.weather.get(0).description
                            binding.ivImage.setImageResource(it.weatherData.weather.get(0).icon.toDrawable())
                            binding.tvWindSpeed.text =
                                it.weatherData.wind.speed.toString() + " ${viewModel.getWindSpeedUnit()}"
                            binding.tvHumidity.text = it.weatherData.main.humidity.toString() + " %"
                            binding.tvPressure.text =
                                it.weatherData.main.pressure.toString() + " hpa"
                            binding.tvCloud.text = it.weatherData.clouds.all.toString() + " %"
                            Log.i("here", "onViewCreated weather Data: " + it.weatherData.weather)
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentForecastWeather.collect {
                    when (it) {
                        is ForecastState.Error -> {
                            Log.i("here", "onViewCreated: forecast " + it.message)
                        }

                        is ForecastState.Loading -> {
                            Log.i("here", "onViewCreated: " + it.toString())
                        }

                        is ForecastState.Success -> {
                            val manager = LinearLayoutManager(requireContext())
                            manager.setOrientation(RecyclerView.HORIZONTAL)
                            val tempUnit: String = viewModel.getUnitTemp()
                            binding.rvHourlyForecast.layoutManager = manager
                            binding.rvHourlyForecast.adapter = HourlyRecyclerViewAdapter(
                                it.forecastData.list.take(8),
                                tempUnit
                            )
                            initDailyItemsUi(it.forecastData.list, tempUnit)
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

    private fun initPermissions() {
        if (checkPermissions()) {
            binding.cvPermissions.visibility = View.GONE
            binding.cvLocation.visibility = View.VISIBLE
            if (isLocationEnabled()) {
                binding.cvLocation.visibility = View.GONE
                binding.clHomeFragment.visibility = View.VISIBLE
                viewModel.getWeatherData()
                viewModel.getForecastWeatherData()
                launchStateFlows()
            }
        } else {
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


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("HEREEE", "onRequestPermResult: " + "Granted?")
        if (requestCode == REQUEST_LOCATION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                binding.cvPermissions.visibility = View.GONE
                binding.cvLocation.visibility = View.VISIBLE
                if (isLocationEnabled()) {
                    binding.cvLocation.visibility = View.GONE
                    binding.clHomeFragment.visibility = View.VISIBLE
                    viewModel.getWeatherData()
                    viewModel.getForecastWeatherData()
                    launchStateFlows()
                }
            }
        }

    }

    fun initDailyItemsUi(data: List<WeatherData>, tempUnit: String) {
        binding.tvTomorrowTemp.text =
            data.get(0).main.temp_min.toString() + " $tempUnit/ " + data.get(
                0
            ).main.temp_max.toString() + "   $tempUnit"
        binding.ivTomorrowWeatherIcon.setImageResource(data[0].weather[0].icon.toDrawable())
        binding.tvTomorrowWeatherDesc.text = data[0].weather[0].description

        binding.tvDay1Name.text = data[8].dt.toDaysTime()
        binding.tvDay1WeatherDesc.text =
            data[8].main.temp_min.toString() + " $tempUnit/ " + data[8].main.temp_max.toString() + "   $tempUnit"
        binding.ivDay1WeatherIcon.setImageResource(data[8].weather[0].icon.toDrawable())
        binding.tvDay1WeatherDesc.text = data[8].weather[0].description

        binding.tvDay2Name.text = data[16].dt.toDaysTime()
        binding.tvDay2WeatherDesc.text =
            data[16].main.temp_min.toString() + " $tempUnit/ " + data[16].main.temp_max.toString() + "   $tempUnit"
        binding.ivDay2WeatherIcon.setImageResource(data[16].weather[0].icon.toDrawable())
        binding.tvDay2WeatherDesc.text = data[16].weather[0].description


    }


}