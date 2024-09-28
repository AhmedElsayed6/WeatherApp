package com.example.weatherapp.data.source.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.data.source.FavData
import kotlinx.coroutines.flow.Flow

interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarm(alarmItem: AlarmItem): Long

    @Query("Select * from FavoritesTable")
    fun getAllAlarms(): Flow<List<AlarmItem>>

    @Delete
    suspend fun deleteAlarm( alarmItem: AlarmItem)
}