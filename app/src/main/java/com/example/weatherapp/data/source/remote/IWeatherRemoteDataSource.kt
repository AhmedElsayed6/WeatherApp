package com.example.weatherapp.data.source.remote

import com.example.weatherapp.data.source.ForecastData
import com.example.weatherapp.data.source.WeatherData
import retrofit2.Response

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeatherData(
        lat: Double,
        lon: Double,
        lang: String
    ): Response<WeatherData>

    suspend fun getForecastWeatherData(
        lat: Double,
        lon: Double,
        lang: String
    ): Response<ForecastData>
}