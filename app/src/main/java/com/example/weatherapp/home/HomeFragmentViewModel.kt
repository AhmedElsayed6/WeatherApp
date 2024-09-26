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

    private val _currentForecastWeatherState = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val currentForecastWeather: StateFlow<ForecastState> = _currentForecastWeatherState.asStateFlow()

    fun getWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getCurrentWeather().collect {
                _currentWeatherState.value = it
            }
        }
    }
    fun getForecastWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getCurrentForecastWeather().collect {
                _currentForecastWeatherState.value = it
            }
        }
    }

    fun getLocationSettings(): String {
        return weatherRepository.getLocationSettings()
    }

    fun getWindSpeedUnit() : String{
        return weatherRepository.getUnitWind()
    }

    fun getUnitTemp() : String{
        return weatherRepository.getUnitTemp()
    }


}