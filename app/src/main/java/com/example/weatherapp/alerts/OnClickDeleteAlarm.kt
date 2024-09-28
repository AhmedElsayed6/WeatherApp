package com.example.weatherapp.alerts

import com.example.weatherapp.data.source.AlarmItem

interface OnClickDeleteAlarm {
    fun deleteAlarm(alarmItem: AlarmItem)
}