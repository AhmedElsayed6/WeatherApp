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
import com.example.weatherapp.databinding.DialogAlertLayoutBinding
import java.time.LocalDateTime
import java.util.Calendar

@SuppressLint("SetTextI18n")
class AlertDialog(
    context: Context, setAlarm: OnClickSetAlarm
) : Dialog(context) {
    private val binding = DialogAlertLayoutBinding.inflate(LayoutInflater.from(context))

    private var selectedDateTimeForData: LocalDateTime = LocalDateTime.now().plusSeconds(10)

    init {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        binding.tvCurrentTime.text =
            "$year/${month + 1}/$day $hour:$minute"
        binding.btnSetAlertTime.setOnClickListener {
            var datePicker =
                DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                    TimePickerDialog(context, { _, selectedHour, selectedMinute ->
                        selectedDateTimeForData = LocalDateTime.of(
                            selectedYear,
                            selectedMonth + 1,
                            selectedDay,
                            selectedHour,
                            selectedMinute
                        )
                        binding.tvCurrentTime.text =
                            "$selectedYear/${selectedMonth + 1}/$selectedDay $selectedHour:$selectedMinute"
                    }, hour, minute, false).show()
                }, year, month, day)
            datePicker.datePicker.minDate = calendar.timeInMillis
            datePicker.show()
        }

        binding.btnSetAlert.setOnClickListener {
            val alarmType =
                findViewById<RadioButton>(binding.radioGroupAlarm.checkedRadioButtonId).text.toString()
            if (alarmType == getString(context, R.string.Alarm))
                setAlarm.setAlarm(selectedDateTimeForData!!, true)
            else
                setAlarm.setAlarm(selectedDateTimeForData!!, false)

            dismiss()
        }


        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))


    }

}