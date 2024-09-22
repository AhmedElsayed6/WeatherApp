package com.example.weatherapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.network.WeatherState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _currentWeatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val currentWeather: StateFlow<WeatherState> = _currentWeatherState.asStateFlow()

    init {
        getWeatherData()
    }

    private fun getWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getCurrentWeather().catch { exception ->
                _currentWeatherState.value = WeatherState.Error(exception.message!!)
            }.collect {
                _currentWeatherState.value = WeatherState.Success(it.body()!!)
            }
        }
    }

    private fun getLocationSettings(){
        weatherRepository.
    }



}