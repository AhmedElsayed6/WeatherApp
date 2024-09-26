package com.example.weatherapp.util

import com.example.weatherapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

fun Long.toDaysTime(): String {
    val date = Date(this * 1000L)
    val format = SimpleDateFormat("EEEE", Locale.getDefault())
    return format.format(date)
}

fun Long.toAMPM(): String {
    val date = Date(this * 1000L)
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return format.format(date)
}


fun Double.toMilesPerHour(): Double {
    val conversionFactor = 2.23694
    return this * conversionFactor
}

fun Double.toKelvin(): Double {
    return this + 273.15
}

fun Double.toFahrenheit(): Double {
    return (this * 9 / 5) + 32
}