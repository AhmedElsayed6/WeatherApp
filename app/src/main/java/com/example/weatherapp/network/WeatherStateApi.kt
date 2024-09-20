package com.example.weatherapp.network

import com.example.weatherapp.data.source.WeatherData

sealed
class WeatherState {
    class Success(val weatherData: WeatherData) : WeatherState()
    class Error(val message: String) : WeatherState()
    object Loading : WeatherState()
}