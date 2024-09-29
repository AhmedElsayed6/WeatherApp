package com.example.weatherapp.data.source.local

import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.data.source.ForecastData
import com.example.weatherapp.data.source.WeatherData
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    suspend fun addToFav(city: String, lat: Double, lon: Double)

    suspend fun addWeatherData(weatherData: WeatherData)
    fun getWeatherData(): Flow<WeatherData>

    suspend fun deleteWeatherData()

    suspend fun addForecastData(forecastData: ForecastData)
    fun getForecastData(): Flow<ForecastData>

    suspend fun deleteForecastData()
    fun getAllFavorites(): Flow<List<FavData>>

    suspend fun deleteFavoriteData(product: FavData)

    suspend fun addAlarm(alarmItem: AlarmItem)
    fun getAllAlarms(): Flow<List<AlarmItem>>

    suspend fun deleteAlarm(alarmItem: AlarmItem)
}
