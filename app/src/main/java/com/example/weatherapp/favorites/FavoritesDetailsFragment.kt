package com.example.weatherapp.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.AppDatabase
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.home.DailyRecyclerViewAdapter
import com.example.weatherapp.home.HomeFragmentViewModel
import com.example.weatherapp.home.HourlyRecyclerViewAdapter
import com.example.weatherapp.network.API
import com.example.weatherapp.network.ForecastState
import com.example.weatherapp.network.WeatherState
import com.example.weatherapp.util.WeatherViewModelFactory
import com.example.weatherapp.util.toAMPM
import com.example.weatherapp.util.toDaysTime
import com.example.weatherapp.util.toDrawable
import kotlinx.coroutines.launch

class FavoritesDetailsFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(
                    AppDatabase.getInstance(requireContext()).weatherDao()
                ),
                WeatherRemoteDataSource.getInstance(API.retrofitService),
                WeatherSharedPreferenceDataSource.getInstance(this.requireContext())
            )
        )
        ViewModelProvider(this, factory)[HomeFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
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
                            binding.clHomeFragment.visibility = View.VISIBLE
                            binding.tvDateAndTime.text =
                                it.weatherData.dt.toDaysTime() + " " + it.weatherData.dt.toAMPM()
                            binding.tvCityName.text = it.weatherData.name
                            binding.tvTemp.text =
                                it.weatherData.main.temp.toString() + " ${viewModel.getUnitTemp()}"
                            binding.tvDescription.text =
                                it.weatherData.weather.get(0).description.replaceFirstChar { it.uppercaseChar() }
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
                            // initDailyItemsUi(it.forecastData.list, tempUnit)
                        }
                    }
                }
            }
        }
    }


}