package com.example.weatherapp.data.source

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
    val humidity: Long
)
//done
data class Wind(
    val speed: Double,
)
//done
data class Clouds(
    val all: Long
)