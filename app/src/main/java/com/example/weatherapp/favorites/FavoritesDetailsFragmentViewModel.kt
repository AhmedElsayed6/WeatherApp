package com.example.weatherapp.favorites

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

class FavoritesDetailsFragmentViewModel(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private val _currentWeatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val currentWeather: StateFlow<WeatherState> = _currentWeatherState.asStateFlow()

    private val _currentForecastWeatherState =
        MutableStateFlow<ForecastState>(ForecastState.Loading)
    val currentForecastWeather: StateFlow<ForecastState> =
        _currentForecastWeatherState.asStateFlow()



    fun getWeatherData(lat:Double , long:Double ) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getCurrentWeather(lat,long).collect {
                _currentWeatherState.value = it
            }
        }
    }

    fun getForecast(lat:Double , long:Double ) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getCurrentForecastWeather(lat,long ).collect {
                _currentForecastWeatherState.value = it
            }
        }
    }


    fun getWindSpeedUnit(): String {
        return weatherRepository.getUnitWind()
    }

    fun getTempUnit(): String {
        return weatherRepository.getUnitTemp()
    }


}