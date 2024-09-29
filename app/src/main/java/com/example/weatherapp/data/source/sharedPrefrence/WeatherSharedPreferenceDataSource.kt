package com.example.weatherapp.data.source.sharedPrefrence

import android.content.Context
import android.content.SharedPreferences



class WeatherSharedPreferenceDataSource private constructor(private val context: Context) :
    IWeatherSharedPreferenceDataSource {

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

    override fun getLanguage(): String {
        return sp.getString("language", "English")!!
    }

    override fun getUnitTemp(): String {
        return sp.getString("temp", "C")!!
    }

    override fun getUnitWind(): String {
        return sp.getString("wind", "ms")!!
    }

    override fun getNotificationSettings(): String {
        return sp.getString("notification", "Enable")!!
    }

    override fun getLocationSettings(): String {
        return sp.getString("location", "GPS")!!
    }

    override fun getLatitude(): Double {
        return sp.getString("lat", "0.0")!!.toDouble()
    }

    override fun getLongitude(): Double {
        return sp.getString("long", "0.0")!!.toDouble()
    }


    override fun setLanguage(language: String) {
        editor.putString("language", language).apply()
    }

    override fun setUnitTemp(unit: String) {
        editor.putString("temp", unit).apply()
    }

    override fun setUnitWind(unit: String) {
        editor.putString("wind", unit).apply()
    }

    override fun setNotificationSettings(key: String) {
        editor.putString("notification", key).apply()
    }

    override fun setLocationSettings(key: String) {
        editor.putString("location", key).apply()
    }

    override fun setLatitude(lat: Double) {
        editor.putString("lat", lat.toString())
    }

    override fun setLongitude(long: Double) {
        editor.putString("long", long.toString())
    }

    override fun getFirstTime(): Boolean {
        return sp.getBoolean("firstTime", true)
    }
    override fun getFirstTimeData(): Boolean {
        return sp.getBoolean("firstTimeData", true)
    }


    override fun setFirstTime() {
        editor.putBoolean("firstTime", false).apply()
    }

    override fun setFirstTimeData() {
        editor.putBoolean("firstTimeData", false).apply()
    }


}