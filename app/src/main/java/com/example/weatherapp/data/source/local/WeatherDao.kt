package com.example.weatherapp.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.source.FavData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFav(favData: FavData): Long

    @Query("Select * from FavoritesTable")
    fun getAllFavorites(): Flow<List<FavData>>


    @Delete
    suspend fun deleteProduct(favData: FavData)
}