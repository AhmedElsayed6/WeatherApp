package com.example.weatherapp.network

import com.example.weatherapp.data.source.ForecastData

sealed
class ForecastState {
    class Success(val forecastData: ForecastData) : ForecastState()
    class Error(val message: String) : ForecastState()
    object Loading : ForecastState()
}