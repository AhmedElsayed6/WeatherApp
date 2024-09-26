package com.example.weatherapp.favorites

import com.example.weatherapp.data.source.FavData

interface OnClickFavoriteDetails {
    fun goToDetails(favData: FavData);
}