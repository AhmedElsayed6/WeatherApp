package com.example.weatherapp.data.source.sharedPrefrence

import android.content.Context
import android.content.SharedPreferences

class WeatherSharedPreferenceDataSource private constructor(private val context: Context) {

    private val sp: SharedPreferences by lazy {
        context.getSharedPreferences("userPreference", Context.MODE_PRIVATE)
    }
    private val editor: SharedPreferences.Editor by lazy {
        sp.edit()
    }

    companion object {
        @Volatile
        private var instance: WeatherSharedPreferenceDataSource? = null

        @Synchronized
        fun getInstance(
            context: Context
        ): WeatherSharedPreferenceDataSource {
            return instance ?: synchronized(this) {
                val tempInstance =
                    WeatherSharedPreferenceDataSource(context)
                instance = tempInstance
                tempInstance
            }
        }
    }

    fun getLanguage(): String {
        return sp.getString("language", "English")!!
    }

    fun getUnitTemp(): String {
        return sp.getString("temp", "C")!!
    }

    fun getUnitWind(): String {
        return sp.getString("wind", "ms")!!
    }

    fun getNotificationSettings(): String {
        return sp.getString("notification", "Enable")!!
    }

    fun getLocationSettings(): String {
        return sp.getString("location", "GPS")!!
    }

    fun getLatitude(): Double {
        return sp.getString("lat", "0.0")!!.toDouble()
    }

    fun getLongitude(): Double {
        return sp.getString("long", "0.0")!!.toDouble()
    }


    fun setLanguage(language: String) {
        editor.putString("language", language).apply()
    }

    fun setUnitTemp(unit: String) {
        editor.putString("temp", unit).apply()
    }

    fun setUnitWind(unit: String) {
        editor.putString("wind", unit).apply()
    }

    fun setNotificationSettings(key: String) {
        editor.putString("notification", key).apply()
    }

    fun setLocationSettings(key: String) {
        editor.putString("location", key).apply()
    }

    fun setLatitude(lat: Double) {
        editor.putString("lat", lat.toString())
    }

    fun setLongitude(long: Double) {
        editor.putString("long", long.toString())
    }

    fun getFirstTime(): Boolean {
        return sp.getBoolean("firstTime", true)
    }
    fun getFirstTimeData(): Boolean {
        return sp.getBoolean("firstTimeData", true)
    }


    fun setFirstTime() {
        editor.putBoolean("firstTime", false).apply()
    }

    fun setFirstTimeData() {
        editor.putBoolean("firstTimeData", false).apply()
    }


}