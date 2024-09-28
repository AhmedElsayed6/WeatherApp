package com.example.weatherapp.alerts

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.RadioButton
import androidx.core.content.ContextCompat.getString
import com.example.weatherapp.R
import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.databinding.DialogAlertLayoutBinding
import com.example.weatherapp.databinding.DialogDeletealarmLayoutBinding
import java.time.LocalDateTime
import java.util.Calendar

@SuppressLint("SetTextI18n")
class ConfirmationDialog(
    context: Context, alarmItem: AlarmItem, deleteAlarm: OnClickDeleteAlarm
) : Dialog(context) {
    private val binding = DialogDeletealarmLayoutBinding.inflate(LayoutInflater.from(context))


    init {
        binding.btnAlarmYes.setOnClickListener{
            deleteAlarm.deleteAlarm(alarmItem)
            dismiss()
        }
        binding.btnAlarmNo.setOnClickListener{dismiss()}
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))


    }

}