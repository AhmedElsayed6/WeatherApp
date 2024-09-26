package com.example.weatherapp.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.FavoritesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _currentWeatherState = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val favoritesState: StateFlow<FavoritesState> = _currentWeatherState.asStateFlow()

    init {
        getAllFavorites()
    }

    private fun getAllFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("here", "getAllFavorites: " + "data ?")
            weatherRepository.getAllFavorites().collect() {
                Log.i("here", "getAllFavorites: " + "collect ?" + it.toString())
                _currentWeatherState.value = it
            }
        }
    }

    fun deleteFavorite(item: FavData) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteProduct(item)
        }
    }
}


