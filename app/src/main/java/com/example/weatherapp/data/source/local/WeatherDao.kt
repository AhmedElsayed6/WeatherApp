package com.example.weatherapp.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.data.source.ForecastData
import com.example.weatherapp.data.source.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFav(favData: FavData): Long

    @Query("Select * from FavoritesTable")
    fun getAllFavorites(): Flow<List<FavData>>

    @Delete
    suspend fun deleteFavoriteData(favData: FavData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeatherData(weatherData: WeatherData): Long

    @Query("Select * from WeatherDataTable")
    fun getWeatherData(): Flow<WeatherData>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addForecastData(forecastData: ForecastData): Long

    @Query("Select * from ForecastDataTable")
    fun getForecastData(): Flow<ForecastData>


    // Function to delete all rows from the weather table
    @Query("DELETE  FROM WeatherDataTable")
    suspend fun deleteAllWeatherDataLocal()

    @Query("DELETE FROM ForecastDataTable")
    suspend fun deleteAllForeCastDataLocal()


}