package com.example.weatherapp.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.source.WeatherRepository

class SettingsFragmentViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {


    fun setLanguage(language: String) {
        weatherRepository.setLanguage(language)
    }

    fun setUnitTemp(unit: String) {
        Log.i("here", "setUnitTemp: in sp VM $unit")
        weatherRepository.setUnitTemp(unit)
    }

    fun setUnitWind(unit: String) {
        weatherRepository.setUnitWind(unit)
    }

    fun setNotificationSettings(key: String) {
        weatherRepository.setNotificationSettings(key)
    }

    fun setLocationSettings(key: String) {
        weatherRepository.setLocationSettings(key)
    }

    fun setLatitude(lat: Double) {
        weatherRepository.setLatitude(lat)
    }

    fun setLongitude(long: Double) {
        weatherRepository.setLongitude(long)
    }

    fun getLocationSettings(): String {
        return weatherRepository.getLocationSettings()
    }

    fun getUnitWind(): String {
        return weatherRepository.getUnitWind()
    }

    fun getUnitTemp(): String {
        return weatherRepository.getUnitTemp()
    }

    fun getNotificationSettings(): String {
        return weatherRepository.getNotificationSettings()
    }

    fun getLanguageSettings(): String {
        return weatherRepository.getLanguageSettings()
    }


}