package com.example.weatherapp.data.source.remote


import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.source.WeatherData
import com.example.weatherapp.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class WeatherRemoteDataSource private constructor(private val apiService: ApiService) {

    private val apiKey = BuildConfig.API_KEY

    companion object {
        @Volatile
        private var instance: WeatherRemoteDataSource? = null

        @Synchronized
        fun getInstance(
            apiService: ApiService
        ): WeatherRemoteDataSource {
            return instance ?: synchronized(this) {
                val tempInstance =
                    WeatherRemoteDataSource(apiService)
                instance = tempInstance
                tempInstance
            }
        }
    }

    fun getCurrentWeatherData(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): Flow<Response<WeatherData>> {
        return flow {
            emit(apiService.getCurrentWeatherData(lat, lon, apiKey, units, lang))
        }
    }


}


