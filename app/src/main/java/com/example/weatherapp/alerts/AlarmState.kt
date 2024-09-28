package com.example.weatherapp.alerts

import com.example.weatherapp.data.source.AlarmItem

sealed
class AlarmState {
    class Success(val alarmData: List<AlarmItem>) : AlarmState()
    class Error(val message: String) : AlarmState()
    object Loading : AlarmState()
}