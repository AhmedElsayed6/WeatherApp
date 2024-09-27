package com.example.weatherapp.favorites

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.example.weatherapp.MainActivity
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.AppDatabase
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.databinding.FragmentFavoritesDetailsBinding
import com.example.weatherapp.home.DailyRecyclerViewAdapter
import com.example.weatherapp.home.HourlyRecyclerViewAdapter
import com.example.weatherapp.network.API
import com.example.weatherapp.network.ForecastState
import com.example.weatherapp.network.WeatherState
import com.example.weatherapp.util.WeatherViewModelFactory
import com.example.weatherapp.util.toAMPM
import com.example.weatherapp.util.toDaysTime
import com.example.weatherapp.util.toDrawable
import com.example.weatherapp.util.toFahrenheit
import com.example.weatherapp.util.toKelvin
import com.example.weatherapp.util.toMilesPerHour
import com.example.weatherapp.util.toTwoDecimalPlaces
import kotlinx.coroutines.launch

class FavoritesDetailsFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesDetailsBinding
    private var favData: FavData? = null
    private val tempUnit: String by lazy {
        viewModel.getTempUnit()
    }
    private val speedUnit: String by lazy {
        viewModel.getWindSpeedUnit()
    }

    private val viewModel: FavoritesDetailsFragmentViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(
                    AppDatabase.getInstance(requireContext()).weatherDao()
                ),
                WeatherRemoteDataSource.getInstance(API.retrofitService),
                WeatherSharedPreferenceDataSource.getInstance(this.requireContext())
            )
        )
        ViewModelProvider(this, factory)[FavoritesDetailsFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favData = arguments?.getSerializable("data") as FavData
        launchStateFlows()
        viewModel.getWeatherData(favData!!.latitude, favData!!.longitude)
        viewModel.getForecast(favData!!.latitude, favData!!.longitude)


    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    private fun launchStateFlows() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentWeather.collect { it ->
                    when (it) {
                        is WeatherState.Error -> {

                        }

                        is WeatherState.Loading -> {

                        }

                        is WeatherState.Success -> {
                            binding.pb2.visibility = View.GONE
                            binding.clHomeFragment.visibility = View.VISIBLE
                            binding.tvDateAndTime.text =
                                it.weatherData.dt.toDaysTime() + " " + it.weatherData.dt.toAMPM()
                            binding.tvCityName.text = favData!!.city
                            when (tempUnit) {
                                "C" -> binding.tvTemp.text =
                                    it.weatherData.main.temp.toTwoDecimalPlaces().toString() + " $tempUnit"+"°"

                                "K" -> binding.tvTemp.text =
                                    it.weatherData.main.temp.toKelvin().toTwoDecimalPlaces().toString() + " $tempUnit"+"°"

                                "F" -> binding.tvTemp.text =
                                    it.weatherData.main.temp.toFahrenheit().toTwoDecimalPlaces()
                                        .toString() + " $tempUnit"+"°"
                            }
                            binding.tvDescription.text =
                                it.weatherData.weather[0].description.replaceFirstChar { it.uppercaseChar() }
                            binding.ivImage.setImageResource(it.weatherData.weather[0].icon.toDrawable())
                            when (speedUnit) {
                                "ms" -> binding.tvWindSpeed.text =
                                    it.weatherData.wind.speed.toString() + " Meter/Sec"
                                "mh" -> binding.tvWindSpeed.text =
                                    it.weatherData.wind.speed.toMilesPerHour().toTwoDecimalPlaces().toString() + " Mile/Hour"

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
                    when (it) {
                        is ForecastState.Error -> {

                        }

                        is ForecastState.Loading -> {

                        }

                        is ForecastState.Success -> {
                            val managerHourly = LinearLayoutManager(requireContext())
                            managerHourly.setOrientation(RecyclerView.HORIZONTAL)
                            val managerDaily = LinearLayoutManager(requireContext())
                            managerDaily.setOrientation(RecyclerView.VERTICAL)
                            val tempUnit: String = viewModel.getTempUnit()
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

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.hide()
    }

    override fun onDestroy() {
        (activity as MainActivity).supportActionBar?.show()
        super.onDestroy()
    }

}