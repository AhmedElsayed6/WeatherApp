package com.example.weatherapp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.favorites.FavoritesDetailsFragmentViewModel
import com.example.weatherapp.favorites.FavoritesFragmentViewModel
import com.example.weatherapp.home.HomeFragmentViewModel
import com.example.weatherapp.initial.InitialSetupViewModel
import com.example.weatherapp.map.MapActivityViewModel

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

            modelClass.isAssignableFrom(MapActivityViewModel::class.java) -> MapActivityViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(FavoritesFragmentViewModel::class.java) -> FavoritesFragmentViewModel(
                repository
            ) as T

            modelClass.isAssignableFrom(FavoritesDetailsFragmentViewModel::class.java) -> FavoritesDetailsFragmentViewModel(
                repository
            ) as T

            else -> throw IllegalArgumentException("ViewModel Not Found")
        }

    }
}