package com.example.weatherapp.alerts

import java.time.LocalDateTime

interface OnClickSetAlarm {
    fun setAlarm(time: LocalDateTime, isAlarm:Boolean )
}