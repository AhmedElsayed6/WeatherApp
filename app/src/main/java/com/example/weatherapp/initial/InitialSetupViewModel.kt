package com.example.weatherapp.initial

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.source.WeatherRepository

class InitialSetupViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {


    fun isFirstTime(): Boolean {
        return weatherRepository.getFirstTime()
    }

    fun setUpData(location: String, language: String, notification: String) {
        if (language == "English")
            weatherRepository.setLanguage("en")
        else
            weatherRepository.setLanguage("ar")

        if (location == "GPS")
            weatherRepository.setLocationSettings("GPS")
        else
            weatherRepository.setLocationSettings("Map")

        if (notification == "Enable")
            weatherRepository.setNotificationSettings("yes")
        else
            weatherRepository.setNotificationSettings("no")

        weatherRepository.setFirstTime()

    }


}