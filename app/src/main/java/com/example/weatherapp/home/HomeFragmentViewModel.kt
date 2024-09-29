package com.example.weatherapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.network.ForecastState
import com.example.weatherapp.network.WeatherState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _currentWeatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val currentWeather: StateFlow<WeatherState> = _currentWeatherState.asStateFlow()

    private val _currentForecastWeatherState =
        MutableStateFlow<ForecastState>(ForecastState.Loading)
    val currentForecastWeather: StateFlow<ForecastState> =
        _currentForecastWeatherState.asStateFlow()


    fun refreshData(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            launch { weatherRepository.deleteCurrentWeatherLocal() }.join()
            launch { weatherRepository.deleteForecastDataLocal() }.join()
            launch {
                getWeatherData(latitude, longitude)
                getForecastWeatherData(latitude,longitude)
            }

        }
    }

    fun getWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getCurrentWeather(latitude, longitude).collect {
                _currentWeatherState.value = it
            }
        }
    }

    fun getWeatherDataLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getCurrentWeatherLocal().collect {
                _currentWeatherState.value = it
            }
        }
    }

    fun getForecastDataLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getForecastLocal().collect {
                _currentForecastWeatherState.value = it
            }
        }
    }

    fun getForecastWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getCurrentForecastWeather(latitude, longitude).collect {
                _currentForecastWeatherState.value = it
            }
        }
    }

    fun getLocationSettings(): String {
        return weatherRepository.getLocationSettings()
    }

    fun getWindSpeedUnit(): String {
        return weatherRepository.getUnitWind()
    }

    fun getUnitTemp(): String {
        return weatherRepository.getUnitTemp()
    }

    fun setFirstTimeData() {
        weatherRepository.setFirstTimeData()
    }

    fun getFirstTimeData(): Boolean {
        return weatherRepository.getFirstTimeData()
    }

    fun setLatitude(lat: Double) {
        weatherRepository.setLatitude(lat)
    }

    fun setLongitude(long: Double) {
        weatherRepository.setLongitude(long)
    }

    fun getLatitude(): Double {
        return weatherRepository.getLatitude()
    }

    fun getLongitude(): Double {
        return weatherRepository.getLongitude()
    }

}