package com.example.weatherapp.data.source.local

import com.example.weatherapp.data.source.FavData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class WeatherLocalDataSource(private val weatherDao: WeatherDao ) {


    companion object {
        @Volatile
        private var instance: WeatherLocalDataSource? = null

        @Synchronized
        fun getInstance(
            weatherDao: WeatherDao
        ): WeatherLocalDataSource {
            return instance ?: synchronized(this) {
                val tempInstance =
                    WeatherLocalDataSource(weatherDao)
                instance = tempInstance
                tempInstance
            }
        }
    }


   suspend fun addToFav(city: String, lat: Double, lon: Double) {
        weatherDao.addToFav(FavData(city, lat, lon))
    }
    fun getAllFavorites(): Flow<List<FavData>> {
        return weatherDao.getAllFavorites()
    }
    suspend fun deleteProduct(product: FavData) {
        weatherDao.deleteProduct(product)
    }

}
