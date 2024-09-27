package com.example.weatherapp.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.source.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapActivityViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {


    fun addToFav(city: String, lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.addToFav(city, lat, lon)
        }

    }

    fun setLongLat(lat: Double, long: Double) {
        weatherRepository.setLatitude(lat)
        weatherRepository.setLongitude(long)
    }


}