package com.example.weatherapp.alerts

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.example.weatherapp.R
import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.databinding.DialogAlarmServiceLayoutBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class AlarmService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var overLayView: View
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var alarmItem: AlarmItem

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        alarmItem = intent?.getSerializableExtra("alarmItem") as AlarmItem? ?: AlarmItem(
            LocalDateTime.now(),
            true,
            "Alarm"
        )
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.alarm_sound);
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        val binding = DialogAlarmServiceLayoutBinding.inflate(LayoutInflater.from(this))
        overLayView = binding.root
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.TOP or Gravity.START
        layoutParams.x = 0
        layoutParams.y = 0
        binding.tvAlarmMessage.text = alarmItem.message
        val dateTime = LocalDateTime.now()
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        binding.tvAlarmCurrentTime.text =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a ")) + " $dayOfWeek"
        binding.btnAlarmDismiss.setOnClickListener {
            mediaPlayer.stop()
            stopSelf()
        }
        windowManager.addView(overLayView, layoutParams)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overLayView)
        mediaPlayer.stop()

    }
}