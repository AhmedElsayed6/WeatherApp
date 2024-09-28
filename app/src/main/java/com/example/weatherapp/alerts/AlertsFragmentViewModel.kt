package com.example.weatherapp.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.data.source.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlertsFragmentViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {


    private val _currentAlarmState = MutableStateFlow<AlarmState>(AlarmState.Loading)
    val alarmState: StateFlow<AlarmState> = _currentAlarmState.asStateFlow()

    init {
        getAllAlarms()
    }


    private fun getAllAlarms() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getAllAlarms().collect() {
                _currentAlarmState.value = it
            }
        }
    }

    fun deleteAlarm(item: AlarmItem) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteAlarm(item)
        }
    }


    fun addAlarm(alarmItem: AlarmItem) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.addAlarm(alarmItem)
        }

    }

}