package com.example.weatherapp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.home.HomeFragmentViewModel
import com.example.weatherapp.initial.InitialSetupViewModel

class WeatherViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeFragmentViewModel::class.java) -> HomeFragmentViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(InitialSetupViewModel::class.java) -> InitialSetupViewModel(
                repository
            ) as T

            else -> throw IllegalArgumentException("ViewModel Not Found")
        }

    }
}