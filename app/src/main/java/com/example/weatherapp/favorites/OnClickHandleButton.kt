package com.example.weatherapp.favorites

import com.example.weatherapp.data.source.FavData

interface OnClickHandleButton {
    fun deleteItem(favData: FavData)
}