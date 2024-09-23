package com.example.weatherapp.network


import com.example.weatherapp.data.source.ForecastData
import com.example.weatherapp.data.source.WeatherData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    suspend fun getCurrentWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String
    ): Response<WeatherData>

    @GET("forecast")
    suspend fun getForecastData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String,
    ): Response<ForecastData>

}


object RetrofitHelper {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val retrofitInstance = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
}

object API {
    val retrofitService by lazy {
        RetrofitHelper.retrofitInstance.create(ApiService::class.java)
    }
}