package com.example.weatherapp.favorites

import com.example.weatherapp.data.source.FavData

interface OnClickDeleteFavorite {
    fun deleteFavorite(favData: FavData)
}