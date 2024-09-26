package com.example.weatherapp.data.source.local

import com.example.weatherapp.data.source.FavData

sealed
class FavoritesState {
    class Success(val favoritesData: List<FavData>) : FavoritesState()
    class Error(val message: String) : FavoritesState()
    object Loading : FavoritesState()
}
