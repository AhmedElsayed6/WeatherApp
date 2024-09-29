package com.example.weatherapp.data.source.local

import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.data.source.ForecastData
import com.example.weatherapp.data.source.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FakeLocal : IWeatherLocalDataSource {
    var weatherData: WeatherData? = null


    private var favorites: MutableList<FavData> = mutableListOf()
    var shouldThrowException = false

    override fun getAllFavorites(): Flow<List<FavData>> = flow {
        if (shouldThrowException) {
            throw Exception("Error retrieving favorites")
        }
        emit(favorites)
    }

    override suspend fun addToFav(city: String, lat: Double, lon: Double) {
        favorites.add(FavData(city,lat,lon))
    }

    override suspend fun addWeatherData(weatherData: WeatherData) {
        this.weatherData=weatherData
    }

    override fun getWeatherData(): Flow<WeatherData> {
       return flow{
            emit(weatherData!!)
        }
    }

    override suspend fun deleteWeatherData() {
        TODO("Not yet implemented")
    }

    override suspend fun addForecastData(forecastData: ForecastData) {
        TODO("Not yet implemented")
    }

    override fun getForecastData(): Flow<ForecastData> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteForecastData() {
        TODO("Not yet implemented")
    }


    override suspend fun deleteFavoriteData(product: FavData) {
        TODO("Not yet implemented")
    }

    override suspend fun addAlarm(alarmItem: AlarmItem) {
        TODO("Not yet implemented")
    }

    override fun getAllAlarms(): Flow<List<AlarmItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarm(alarmItem: AlarmItem) {
        TODO("Not yet implemented")
    }



}