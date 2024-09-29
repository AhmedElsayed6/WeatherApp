package com.example.weatherapp.data.source.sharedPrefrence

interface IWeatherSharedPreferenceDataSource {
    fun getLanguage(): String
    fun getUnitTemp(): String
    fun getUnitWind(): String
    fun getNotificationSettings(): String
    fun getLocationSettings(): String
    fun getLatitude(): Double
    fun getLongitude(): Double
    fun setLanguage(language: String)
    fun setUnitTemp(unit: String)
    fun setUnitWind(unit: String)
    fun setNotificationSettings(key: String)
    fun setLocationSettings(key: String)
    fun setLatitude(lat: Double)
    fun setLongitude(long: Double)
    fun getFirstTime(): Boolean
    fun getFirstTimeData(): Boolean
    fun setFirstTime()
    fun setFirstTimeData()
}