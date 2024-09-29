package com.example.weatherapp.data.source

import com.example.weatherapp.alerts.AlarmState
import com.example.weatherapp.data.source.local.FavoritesState
import com.example.weatherapp.network.ForecastState
import com.example.weatherapp.network.WeatherState
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    fun getCurrentWeather(lat: Double, long: Double): Flow<WeatherState>
    fun getCurrentWeatherLocal(): Flow<WeatherState>

    suspend fun deleteCurrentWeatherLocal()

    suspend fun deleteForecastDataLocal()
    fun getForecastLocal(): Flow<ForecastState>
    fun getCurrentForecastWeather(lat: Double = 10.0, long: Double = 10.0): Flow<ForecastState>
    fun setFirstTime()
    fun getFirstTime(): Boolean
    fun setFirstTimeData()
    fun getFirstTimeData(): Boolean
    fun setLanguage(language: String)
    fun setUnitTemp(unit: String)
    fun setUnitWind(unit: String)
    fun setNotificationSettings(key: String)
    fun setLocationSettings(key: String)
    fun setLatitude(lat: Double)
    fun setLongitude(long: Double)
    fun getLatitude(): Double
    fun getLongitude(): Double
    fun getNotificationSettings(): String
    fun getLanguageSettings(): String
    fun getLocationSettings(): String
    fun getUnitWind(): String
    fun getUnitTemp(): String

    suspend fun addToFav(city: String, lat: Double, lon: Double)

    suspend fun getAllFavorites(): Flow<FavoritesState>

    suspend fun deleteFavoriteData(favData: FavData)

    suspend fun addAlarm(alarmItem: AlarmItem)
    fun getAllAlarms(): Flow<AlarmState>

    suspend fun deleteAlarm(alarmItem: AlarmItem)
}