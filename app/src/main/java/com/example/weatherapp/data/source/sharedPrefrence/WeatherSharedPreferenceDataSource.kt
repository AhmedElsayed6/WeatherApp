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
        return sp.getString("language", "en")!!
    }

    fun getUnitTemp(): String {
        return sp.getString("temp", "standard")!!
    }

    fun getUnitPressure(): String {
        return sp.getString("pressure", "standard")!!
    }

    fun getNotificationSettings(): String {
        return sp.getString("notification", "yes")!!
    }

    fun getLocationSettings(): String {
        return sp.getString("location", "GPS")!!
    }

    fun getLatitude(): Double {
        return sp.getLong("lat", 0L).toDouble()
    }

    fun getLongitude(): Double {
        return sp.getLong("long", 0L).toDouble()
    }

    fun getFirstTime():Boolean {
       return sp.getBoolean("firstTime",true)
    }
    fun setFirstTime(){
        editor.putBoolean("firstTime",false).apply()
    }
    fun setLanguage(language: String) {
        editor.putString("language", language).apply()
    }

    fun setUnitTemp(unit: String) {
        editor.putString("temp", unit).apply()
    }

    fun setUnitPressure(unit: String) {
        editor.putString("pressure", unit).apply()
    }

    fun setNotificationSettings(key: String) {
        editor.putString("notification", key).apply()
    }

    fun setLocationSettings(key: String) {
        editor.putString("location", key).apply()
    }

    fun setLatitude(lat: Double) {
        editor.putLong("lat", lat.toLong())
    }

    fun setLongitude(long: Double) {
        editor.putLong("long", long.toLong())
    }



}