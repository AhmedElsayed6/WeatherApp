package com.example.weatherapp.alerts

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R

import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.AppDatabase
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.initial.InitialSetupActivity
import com.example.weatherapp.network.API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var context: Context
    private lateinit var notificationManager: NotificationManager
    private lateinit var item: AlarmItem
    private val repository by lazy {
        WeatherRepository.getInstance(
            WeatherLocalDataSource.getInstance(
                AppDatabase.getInstance(context).weatherDao(),
                AppDatabase.getInstance(context).alarmDao()
            ),
            WeatherRemoteDataSource.getInstance(API.retrofitService),
            WeatherSharedPreferenceDataSource.getInstance(context)
        )
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context!!
        item = intent?.getSerializableExtra("alarmItem") as AlarmItem? ?: return
        Log.i("here", "onReceive: BCR" + item.message)
        showNotification()
    }


    private fun showNotification() {
        createNotificationChannel()
        val activityIntent = Intent(context, InitialSetupActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val notification =
            NotificationCompat.Builder(context, "Notification").setSmallIcon(R.drawable.icon_alarm)
                .setContentTitle("Weather Notification").setContentText("This is an alarm")
                .setContentIntent(
                    pendingIntent
                )
                .setAutoCancel(true)
                .build()
        notificationManager.notify(item.hashCode(), notification)
        GlobalScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(item)
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Notification", "Notification", NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Used for the alarm notifications"
            channel.enableLights(true)
            channel.enableVibration(true)

            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}