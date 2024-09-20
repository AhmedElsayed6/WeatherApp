package com.example.weatherapp.data.source

import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class WeatherRepository private constructor(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) {

    companion object {
        private var instance: WeatherRepository? = null
        fun getInstance(
            weatherLocalDataSource: WeatherLocalDataSource,
            weatherRemoteDataSource: WeatherRemoteDataSource
        ): WeatherRepository {
            return instance ?: synchronized(this) {
                val tempInstance =
                    WeatherRepository(weatherLocalDataSource, weatherRemoteDataSource)
                instance = tempInstance
                tempInstance
            }
        }
    }



        fun getCurrentWeather(): Flow<Response<WeatherData>>{
          return  weatherRemoteDataSource.getCurrentWeatherData(12.0,12.0,"metric","en")
        }


}