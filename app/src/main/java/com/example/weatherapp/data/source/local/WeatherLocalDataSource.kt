package com.example.weatherapp.data.source.local

import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.data.source.ForecastData
import com.example.weatherapp.data.source.WeatherData
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private val weatherDao: WeatherDao, private val alarmDao: AlarmDao) {


    companion object {
        @Volatile
        private var instance: WeatherLocalDataSource? = null

        @Synchronized
        fun getInstance(
            weatherDao: WeatherDao, alarmDao: AlarmDao
        ): WeatherLocalDataSource {
            return instance ?: synchronized(this) {
                val tempInstance =
                    WeatherLocalDataSource(weatherDao, alarmDao)
                instance = tempInstance
                tempInstance
            }
        }
    }


    suspend fun addToFav(city: String, lat: Double, lon: Double) {
        weatherDao.addToFav(FavData(city, lat, lon))
    }

    suspend fun addWeatherData(weatherData: WeatherData) {
        weatherDao.addWeatherData(weatherData)
    }

    fun getWeatherData(): Flow<WeatherData> {
        return weatherDao.getWeatherData()
    }

    suspend fun deleteWeatherData(weatherData: WeatherData) {
        weatherDao.deleteWeatherData(weatherData)
    }


    suspend fun addForecastData(forecastData: ForecastData) {
        weatherDao.addForecastData(forecastData)
    }

    fun getForecastData(): Flow<ForecastData> {
        return weatherDao.getForecastData()
    }

    suspend fun deleteForecastData(forecastData: ForecastData) {
        weatherDao.deleteForecastData(forecastData)
    }

    fun getAllFavorites(): Flow<List<FavData>> {
        return weatherDao.getAllFavorites()
    }


    suspend fun deleteProduct(product: FavData) {
        weatherDao.deleteProduct(product)
    }


    suspend fun addAlarm(alarmItem: AlarmItem) {
        alarmDao.addAlarm(alarmItem)
    }

    fun getAllAlarms(): Flow<List<AlarmItem>> {
        return alarmDao.getAllAlarms()
    }

    suspend fun deleteAlarm(alarmItem: AlarmItem) {
        alarmDao.deleteAlarm(alarmItem)
    }


}
