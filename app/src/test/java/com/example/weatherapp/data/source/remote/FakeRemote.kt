package com.example.weatherapp.data.source.remote

import com.example.weatherapp.data.source.ForecastData
import com.example.weatherapp.data.source.WeatherData
import okhttp3.ResponseBody
import retrofit2.Response


class FakeRemote: IWeatherRemoteDataSource {
    var shouldReturnSuccessfulResponse = true
    var weatherData: WeatherData? = null

    override suspend fun getCurrentWeatherData(lat: Double, long: Double, lang: String): Response<WeatherData> {
        return if (shouldReturnSuccessfulResponse) {
            Response.success(weatherData)
        } else {
            Response.error(400, ResponseBody.create(null, "Error"))
        }
    }

    override suspend fun getForecastWeatherData(
        lat: Double,
        lon: Double,
        lang: String
    ): Response<ForecastData> {
        TODO("Not yet implemented")
    }
}