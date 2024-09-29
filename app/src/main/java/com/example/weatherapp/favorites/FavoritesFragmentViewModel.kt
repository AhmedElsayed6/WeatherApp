package com.example.weatherapp.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.data.source.IWeatherRepository
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.FavoritesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(private val weatherRepository: IWeatherRepository) : ViewModel() {

    private val _currentWeatherState = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val favoritesState: StateFlow<FavoritesState> = _currentWeatherState.asStateFlow()

    init {
        getAllFavorites()
    }

    fun getAllFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getAllFavorites().collect() {
                _currentWeatherState.value = it
            }
        }
    }

    fun deleteFavorite(item: FavData) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteFavoriteData(item)
        }
    }
}


