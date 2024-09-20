package com.example.weatherapp.util

import com.example.weatherapp.R

fun String.toDrawable(): Int {
    when (this) {
        "01d" -> return R.drawable.i01d
        "01n" -> return R.drawable.i01n
        "02d" -> return R.drawable.i02d
        "02n" -> return R.drawable.i02n
        "03d" -> return R.drawable.i03d
        "03n" -> return R.drawable.i03n
        "04d" -> return R.drawable.i04d
        "04n" -> return R.drawable.i04n
        "09d" -> return R.drawable.i09d
        "09n" -> return R.drawable.i09n
        "10d" -> return R.drawable.i10d
        "10n" -> return R.drawable.i10n
        "11d" -> return R.drawable.i11d
        "11n" -> return R.drawable.i11n
        "13d" -> return R.drawable.i13d
        "13n" -> return R.drawable.i13n
        "50d" -> return R.drawable.i50d
        "50n" -> return R.drawable.i50n
        else -> return R.drawable.ic_launcher_foreground
    }
}