package com.example.weatherapp.data.source

import android.util.Log
import com.example.weatherapp.alerts.AlarmState
import com.example.weatherapp.data.source.local.FavoritesState
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.network.ForecastState
import com.example.weatherapp.network.WeatherState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository private constructor(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherSharedPreferenceDataSource: WeatherSharedPreferenceDataSource
) {

    companion object {
        @Volatile
        private var instance: WeatherRepository? = null

        @Synchronized
        fun getInstance(
            weatherLocalDataSource: WeatherLocalDataSource,
            weatherRemoteDataSource: WeatherRemoteDataSource,
            weatherSharedPreferenceDataSource: WeatherSharedPreferenceDataSource
        ): WeatherRepository {
            return instance ?: synchronized(this) {
                val tempInstance = WeatherRepository(
                    weatherLocalDataSource,
                    weatherRemoteDataSource,
                    weatherSharedPreferenceDataSource
                )
                instance = tempInstance
                tempInstance
            }
        }
    }


    fun getCurrentWeather(lat: Double = 10.0, long: Double = 10.0): Flow<WeatherState> = flow {

        try {
            val response = weatherRemoteDataSource.getCurrentWeatherData(
                lat,
                long,
                if (getLanguageSettings() == "English") "en" else "ar"
            )
            if (response.isSuccessful) {
                emit(WeatherState.Success(response.body()!!))
            } else emit(WeatherState.Error("Error Found"))
        } catch (e: Exception) {
            emit(WeatherState.Error("Error Found"))
        }
    }

    fun getCurrentForecastWeather(lat: Double = 10.0, long: Double = 10.0): Flow<ForecastState> =

        flow {
            try {
                val response = weatherRemoteDataSource.getForecastWeatherData(
                    lat,
                    long,
                    if (getLanguageSettings() == "English") "en" else "ar"
                )
                if (response.isSuccessful) {
                    emit(ForecastState.Success(response.body()!!))
                } else emit(ForecastState.Error(response.message()))
            } catch (e: Exception) {
                emit(ForecastState.Error(e.message!!))
            }
        }


    fun setFirstTime() {
        weatherSharedPreferenceDataSource.setFirstTime()
    }

    fun getFirstTime(): Boolean {
        return weatherSharedPreferenceDataSource.getFirstTime()
    }

    fun setLanguage(language: String) {
        weatherSharedPreferenceDataSource.setLanguage(language)
    }

    fun setUnitTemp(unit: String) {
        Log.i("here", "setUnitTemp: in sp $unit")
        weatherSharedPreferenceDataSource.setUnitTemp(unit)
    }

    fun setUnitWind(unit: String) {
        weatherSharedPreferenceDataSource.setUnitWind(unit)
    }

    fun setNotificationSettings(key: String) {
        weatherSharedPreferenceDataSource.setNotificationSettings(key)
    }

    fun setLocationSettings(key: String) {
        weatherSharedPreferenceDataSource.setLocationSettings(key)
    }

    fun setLatitude(lat: Double) {
        weatherSharedPreferenceDataSource.setLatitude(lat)
    }

    fun setLongitude(long: Double) {
        weatherSharedPreferenceDataSource.setLongitude(long)
    }

    fun getNotificationSettings(): String {
        return weatherSharedPreferenceDataSource.getNotificationSettings()
    }

    fun getLanguageSettings(): String {
        return weatherSharedPreferenceDataSource.getLanguage()
    }

    fun getLocationSettings(): String {
        return weatherSharedPreferenceDataSource.getLocationSettings()
    }

    fun getUnitWind(): String {
        return weatherSharedPreferenceDataSource.getUnitWind()
    }

    fun getUnitTemp(): String {
        return weatherSharedPreferenceDataSource.getUnitTemp()
    }

    suspend fun addToFav(city: String, lat: Double, lon: Double) {
        weatherLocalDataSource.addToFav(city, lat, lon)
    }

    suspend fun getAllFavorites(): Flow<FavoritesState> {
        return flow {
            weatherLocalDataSource.getAllFavorites().collect() { favList ->
                emit(FavoritesState.Success(favList))
            }
        }

    }

    suspend fun deleteProduct(product: FavData) {
        weatherLocalDataSource.deleteProduct(product)
    }


    suspend fun addAlarm(alarmItem: AlarmItem) {
        weatherLocalDataSource.addAlarm(alarmItem)
    }

    fun getAllAlarms(): Flow<AlarmState> {
        return flow {
            weatherLocalDataSource.getAllAlarms().collect() { alarmData ->
                emit(AlarmState.Success(alarmData))
            }
        }
    }

    suspend fun deleteAlarm(alarmItem: AlarmItem) {
        weatherLocalDataSource.deleteAlarm(alarmItem)
    }


}