package com.example.weatherapp.initial

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.source.WeatherRepository

class InitialSetupViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {


    fun isFirstTime(): Boolean {
        return weatherRepository.getFirstTime()
    }

    fun setUpData(location: String, language: String, notification: String) {
        if (language == "English")
            weatherRepository.setLanguage("English")
        else
            weatherRepository.setLanguage("Arabic")

        if (location == "GPS")
            weatherRepository.setLocationSettings("GPS")
        else
            weatherRepository.setLocationSettings("Map")

        if (notification == "Enable")
            weatherRepository.setNotificationSettings("Enable")
        else
            weatherRepository.setNotificationSettings("Disable")

        weatherRepository.setFirstTime()

    }


}