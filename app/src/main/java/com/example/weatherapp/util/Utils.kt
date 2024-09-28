package com.example.weatherapp.util

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.TypeConverter
import com.example.weatherapp.R
import com.example.weatherapp.data.source.Weather
import com.example.weatherapp.data.source.WeatherData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

fun Double.toTwoDecimalPlaces(): Double {
    return String.format("%.2f", this).toDouble()
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return capabilities != null
}


object Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String? {
        return localDateTime?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(dateString: String?): LocalDateTime? {
        return dateString?.let {
            return LocalDateTime.parse(it, formatter)
        }

    }

    @TypeConverter
    fun fromWeatherList(value: List<Weather>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toWeatherList(value: String): List<Weather> {
        val listType = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromWeatherDataList(value: List<WeatherData>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toWeatherDataList(value: String): List<WeatherData> {
        val listType = object : TypeToken<List<WeatherData>>() {}.type
        return Gson().fromJson(value, listType)
    }

    fun checkAndChangLocality() {
//        val languageCode = if(sharedViewModel.settingsLanguage.value == Constants.ENGLISH_SELECTION_VALUE) "en" else "ar"
//        val locale = resources.configuration.locales[0]
//
//        if(locale.language != languageCode)
//        {
//
//            val newLocale = Locale(languageCode)
//            Locale.setDefault(newLocale)
//
//            val config = resources.configuration
//
//            config.setLocale(newLocale)
//            config.setLayoutDirection(newLocale)
//
//            resources.updateConfiguration(config,resources.displayMetrics)
//
//            recreate()
//
//        }
    }
}