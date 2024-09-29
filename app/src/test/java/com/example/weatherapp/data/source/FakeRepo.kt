package com.example.weatherapp.data.source

import com.example.weatherapp.alerts.AlarmState
import com.example.weatherapp.data.source.local.FavoritesState
import com.example.weatherapp.network.ForecastState
import com.example.weatherapp.network.WeatherState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FakeRepo : IWeatherRepository {

    private val favoriteList = mutableListOf<FavData>()

    override suspend fun addToFav(city: String, lat: Double, lon: Double) {
        favoriteList.add(FavData(city, lat, lon))
    }

    override suspend fun getAllFavorites(): Flow<FavoritesState> {
        return flow {
            if(favoriteList.isNotEmpty())
            emit(FavoritesState.Success(favoriteList))
            else
                emit(FavoritesState.Error("NoData"))
        }
    }

    override suspend fun deleteFavoriteData(product: FavData) {
        favoriteList.remove(product)
    }
    override fun getCurrentWeather(lat: Double, long: Double): Flow<WeatherState> {
        TODO("Not yet implemented")
    }

    override fun getCurrentWeatherLocal(): Flow<WeatherState> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrentWeatherLocal() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteForecastDataLocal() {
        TODO("Not yet implemented")
    }

    override fun getForecastLocal(): Flow<ForecastState> {
        TODO("Not yet implemented")
    }

    override fun getCurrentForecastWeather(lat: Double, long: Double): Flow<ForecastState> {
        TODO("Not yet implemented")
    }

    override fun setFirstTime() {
        TODO("Not yet implemented")
    }

    override fun getFirstTime(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setFirstTimeData() {
        TODO("Not yet implemented")
    }

    override fun getFirstTimeData(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setLanguage(language: String) {
        TODO("Not yet implemented")
    }

    override fun setUnitTemp(unit: String) {
        TODO("Not yet implemented")
    }

    override fun setUnitWind(unit: String) {
        TODO("Not yet implemented")
    }

    override fun setNotificationSettings(key: String) {
        TODO("Not yet implemented")
    }

    override fun setLocationSettings(key: String) {
        TODO("Not yet implemented")
    }

    override fun setLatitude(lat: Double) {
        TODO("Not yet implemented")
    }

    override fun setLongitude(long: Double) {
        TODO("Not yet implemented")
    }

    override fun getLatitude(): Double {
        TODO("Not yet implemented")
    }

    override fun getLongitude(): Double {
        TODO("Not yet implemented")
    }

    override fun getNotificationSettings(): String {
        TODO("Not yet implemented")
    }

    override fun getLanguageSettings(): String {
        TODO("Not yet implemented")
    }

    override fun getLocationSettings(): String {
        TODO("Not yet implemented")
    }

    override fun getUnitWind(): String {
        TODO("Not yet implemented")
    }

    override fun getUnitTemp(): String {
        TODO("Not yet implemented")
    }


    override suspend fun addAlarm(alarmItem: AlarmItem) {
        TODO("Not yet implemented")
    }

    override fun getAllAlarms(): Flow<AlarmState> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarm(alarmItem: AlarmItem) {
        TODO("Not yet implemented")
    }


}