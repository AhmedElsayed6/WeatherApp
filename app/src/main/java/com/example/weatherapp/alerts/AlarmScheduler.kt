package com.example.weatherapp.alerts

import com.example.weatherapp.data.source.AlarmItem

interface AlarmScheduler {
    fun scheduleAlarm(item:AlarmItem)
    fun cancelAlarm(item: AlarmItem)
}