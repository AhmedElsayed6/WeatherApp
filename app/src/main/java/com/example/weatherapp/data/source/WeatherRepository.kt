package com.example.weatherapp.data.source

import android.util.Log
import com.example.weatherapp.alerts.AlarmState
import com.example.weatherapp.data.source.local.FavoritesState
import com.example.weatherapp.data.source.local.IWeatherLocalDataSource
import com.example.weatherapp.data.source.remote.IWeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.IWeatherSharedPreferenceDataSource
import com.example.weatherapp.network.ForecastState
import com.example.weatherapp.network.WeatherState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class WeatherRepository private constructor(
    private val weatherLocalDataSource: IWeatherLocalDataSource,
    private val weatherRemoteDataSource: IWeatherRemoteDataSource,
    private val weatherSharedPreferenceDataSource: IWeatherSharedPreferenceDataSource
) : IWeatherRepository {

    companion object {
        @Volatile
        private var instance: WeatherRepository? = null

        @Synchronized
        fun getInstance(
            weatherLocalDataSource: IWeatherLocalDataSource,
            weatherRemoteDataSource: IWeatherRemoteDataSource,
            weatherSharedPreferenceDataSource: IWeatherSharedPreferenceDataSource
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


    override fun getCurrentWeather(lat: Double, long: Double): Flow<WeatherState> = flow {
        try {
            val response = weatherRemoteDataSource.getCurrentWeatherData(
                lat,
                long,
                if (getLanguageSettings() == "English") "en" else "ar"
            )
            if (response.isSuccessful) {
                weatherLocalDataSource.addWeatherData(response.body()!!)
                weatherLocalDataSource.getWeatherData()
                    .catch { emit(WeatherState.Error("Error Found")) }.collect {
                        weatherSharedPreferenceDataSource.setFirstTimeData()
                        emit(WeatherState.Success(it))
                    }
            } else emit(WeatherState.Error("Error Found"))
        } catch (e: Exception) {
            emit(WeatherState.Error("Error Found"))
        }
    }

    override fun getCurrentWeatherLocal(): Flow<WeatherState> = flow {
        weatherLocalDataSource.getWeatherData()
            .catch { emit(WeatherState.Error("Error Found")) }.collect {
                if (it != null)
                    emit(WeatherState.Success(it))
            }
    }

    override suspend fun deleteCurrentWeatherLocal() {
        weatherLocalDataSource.deleteWeatherData()

    }

    override suspend fun deleteForecastDataLocal() {
        weatherLocalDataSource.deleteForecastData()
    }


    override fun getForecastLocal(): Flow<ForecastState> = flow {
        weatherLocalDataSource.getForecastData()
            .catch { emit(ForecastState.Error("Error Found")) }.collect {
                if (it != null)
                    emit(ForecastState.Success(it))
            }
    }

    override fun getCurrentForecastWeather(lat: Double, long: Double): Flow<ForecastState> =
        flow {
            try {
                val response = weatherRemoteDataSource.getForecastWeatherData(
                    lat,
                    long,
                    if (getLanguageSettings() == "English") "en" else "ar"
                )
                if (response.isSuccessful) {
                    weatherLocalDataSource.addForecastData(response.body()!!)
                    weatherLocalDataSource.getForecastData()
                        .catch { emit(ForecastState.Error("Error Found")) }.collect {
                            weatherSharedPreferenceDataSource.setFirstTimeData()
                            if (it != null)
                                emit(ForecastState.Success(it))
                            else
                                emit(ForecastState.Error("Error Found"))
                        }
                } else emit(ForecastState.Error(response.message()))
            } catch (e: Exception) {
                emit(ForecastState.Error(e.message!!))
            }
        }


    override fun setFirstTime() {
        weatherSharedPreferenceDataSource.setFirstTime()
    }

    override fun getFirstTime(): Boolean {
        return weatherSharedPreferenceDataSource.getFirstTime()
    }

    override fun setFirstTimeData() {
        weatherSharedPreferenceDataSource.setFirstTimeData()
    }

    override fun getFirstTimeData(): Boolean {
        return weatherSharedPreferenceDataSource.getFirstTimeData()
    }

    override fun setLanguage(language: String) {
        weatherSharedPreferenceDataSource.setLanguage(language)
    }

    override fun setUnitTemp(unit: String) {
        Log.i("here", "setUnitTemp: in sp $unit")
        weatherSharedPreferenceDataSource.setUnitTemp(unit)
    }

    override fun setUnitWind(unit: String) {
        weatherSharedPreferenceDataSource.setUnitWind(unit)
    }

    override fun setNotificationSettings(key: String) {
        weatherSharedPreferenceDataSource.setNotificationSettings(key)
    }

    override fun setLocationSettings(key: String) {
        weatherSharedPreferenceDataSource.setLocationSettings(key)
    }

    override fun setLatitude(lat: Double) {
        weatherSharedPreferenceDataSource.setLatitude(lat)
    }

    override fun setLongitude(long: Double) {
        weatherSharedPreferenceDataSource.setLongitude(long)
    }

    override fun getLatitude(): Double {
        return weatherSharedPreferenceDataSource.getLatitude()
    }

    override fun getLongitude(): Double {
        return weatherSharedPreferenceDataSource.getLongitude()
    }

    override fun getNotificationSettings(): String {
        return weatherSharedPreferenceDataSource.getNotificationSettings()
    }

    override fun getLanguageSettings(): String {
        return weatherSharedPreferenceDataSource.getLanguage()
    }

    override fun getLocationSettings(): String {
        return weatherSharedPreferenceDataSource.getLocationSettings()
    }

    override fun getUnitWind(): String {
        return weatherSharedPreferenceDataSource.getUnitWind()
    }

    override fun getUnitTemp(): String {
        return weatherSharedPreferenceDataSource.getUnitTemp()
    }

    override suspend fun addToFav(city: String, lat: Double, lon: Double) {
        weatherLocalDataSource.addToFav(city, lat, lon)
    }

    override suspend fun getAllFavorites(): Flow<FavoritesState> {
        return flow {
            weatherLocalDataSource.getAllFavorites().collect() { favList ->
                emit(FavoritesState.Success(favList))
            }
        }

    }

    override suspend fun deleteFavoriteData(product: FavData) {
        weatherLocalDataSource.deleteFavoriteData(product)
    }


    override suspend fun addAlarm(alarmItem: AlarmItem) {
        weatherLocalDataSource.addAlarm(alarmItem)
    }

    override fun getAllAlarms(): Flow<AlarmState> {
        return flow {
            weatherLocalDataSource.getAllAlarms().collect() { alarmData ->
                emit(AlarmState.Success(alarmData))
            }
        }
    }

    override suspend fun deleteAlarm(alarmItem: AlarmItem) {
        weatherLocalDataSource.deleteAlarm(alarmItem)
    }


}