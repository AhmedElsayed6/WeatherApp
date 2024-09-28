package com.example.weatherapp.alerts

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
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
        if (repository.getNotificationSettings() == "Enable") {
            if (Settings.canDrawOverlays(context)) {
                if (item.isAlarm) {
                    showNotification()
                    val intent = Intent(context, AlarmService::class.java)
                    intent.putExtra("alarmItem", item)
                    context.startService(intent)
                } else {
                    showNotification()
                }
            }
        }


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


//    fun startTheAlertService(context: Context, title: String, description: String) {
//        val intent = Intent(context, MyDialogService::class.java).apply {
//            putExtra("title", title)
//            putExtra("description", description)
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            val notificationManager = context.getSystemService(NotificationManager::class.java) as NotificationManager
//            if (notificationManager.areNotificationsEnabled()) {
//                context.startForegroundService(intent)
//            } else {
//                // Handle the permission request for notifications if needed
//                ActivityCompat.requestPermissions(context as Activity,
//                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
//            }
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Start the foreground service for Android O and above
//            context.startForegroundService(intent)
//        } else {
//            // Start a regular service for Android versions lower than O
//            context.startService(intent)
//        }
//    }

}