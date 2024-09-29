package com.example.weatherapp.data.source.remote


import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.source.ForecastData
import com.example.weatherapp.data.source.WeatherData
import com.example.weatherapp.network.ApiService
import retrofit2.Response

class WeatherRemoteDataSource private constructor(private val apiService: ApiService) :
    IWeatherRemoteDataSource {

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

    override suspend fun getCurrentWeatherData(
        lat: Double,
        lon: Double,
        lang: String
    ): Response<WeatherData> {
        return apiService.getCurrentWeatherData(lat, lon, apiKey, lang)

    }

    override suspend fun getForecastWeatherData(
        lat: Double,
        lon: Double,
        lang: String
    ): Response<ForecastData> {
        return apiService.getForecastData(lat, lon, apiKey, lang)

    }


}


