package com.example.weatherapp.data.source.local

import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.data.source.ForecastData
import com.example.weatherapp.data.source.WeatherData
import kotlinx.coroutines.flow.Flow


class WeatherLocalDataSource(private val weatherDao: WeatherDao, private val alarmDao: AlarmDao) :
    IWeatherLocalDataSource {


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


    override suspend fun addToFav(city: String, lat: Double, lon: Double) {
        weatherDao.addToFav(FavData(city, lat, lon))
    }

    override suspend fun addWeatherData(weatherData: WeatherData) {
        weatherDao.addWeatherData(weatherData)
    }

    override fun getWeatherData(): Flow<WeatherData> {
        return weatherDao.getWeatherData()
    }

    override suspend fun deleteWeatherData() {
        weatherDao.deleteAllWeatherDataLocal()
    }


    override suspend fun addForecastData(forecastData: ForecastData) {
        weatherDao.addForecastData(forecastData)
    }

    override fun getForecastData(): Flow<ForecastData> {
        return weatherDao.getForecastData()
    }

    override suspend fun deleteForecastData() {
        weatherDao.deleteAllForeCastDataLocal()
    }

    override fun getAllFavorites(): Flow<List<FavData>> {
        return weatherDao.getAllFavorites()
    }


    override suspend fun deleteFavoriteData(product: FavData) {
        weatherDao.deleteFavoriteData(product)
    }


    override suspend fun addAlarm(alarmItem: AlarmItem) {
        alarmDao.addAlarm(alarmItem)
    }

    override fun getAllAlarms(): Flow<List<AlarmItem>> {
        return alarmDao.getAllAlarms()
    }

    override suspend fun deleteAlarm(alarmItem: AlarmItem) {
        alarmDao.deleteAlarm(alarmItem)
    }


}
