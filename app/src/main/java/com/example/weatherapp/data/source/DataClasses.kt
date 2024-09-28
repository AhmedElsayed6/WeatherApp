package com.example.weatherapp.data.source

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime

data class WeatherData(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    //city name
    val name: String,
)

data class ForecastData(
    val list: List<WeatherData>,
    val city: City
)

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

// done
data class Coord(
    val lon: Double,
    val lat: Double
)

//done
data class Weather(
    val id: Long,
    val description: String,
    val icon: String
)

// done
data class Main(
    val temp: Double,
    val pressure: Long,
    val humidity: Long,
    val temp_min: Double,
    val temp_max: Double
)

//done
data class Wind(
    val speed: Double,
)

//done
data class Clouds(
    val all: Long
)

@Entity(tableName = "FavoritesTable")
data class FavData(
    var city: String, var latitude: Double, var longitude: Double
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity(tableName = "AlarmsTable")
data class AlarmItem(val time: LocalDateTime, val isAlarm: Boolean, val message: String) :
    Serializable {
    @PrimaryKey
    var id: Int = this.hashCode()
}