package com.example.weatherapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.data.source.ForecastData
import com.example.weatherapp.data.source.WeatherData
import com.example.weatherapp.util.Converters


@Database(entities = [ForecastData::class , WeatherData::class , FavData::class , AlarmItem::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun alarmDao():AlarmDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Weather_Database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

    }
}