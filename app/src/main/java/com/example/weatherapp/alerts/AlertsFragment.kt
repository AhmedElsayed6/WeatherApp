package com.example.weatherapp.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.databinding.FragmentAlertsBinding
import java.time.LocalDateTime


class AlertsFragment : Fragment(), OnClickSetAlarm, OnClickDeleteAlarm {

    private lateinit var binding: FragmentAlertsBinding
    private val scheduler by lazy {
        AlarmSchedulerImp(this.requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddAlert.setOnClickListener {

            val alertDialog = AlertDialog(this.requireContext(), this)
            alertDialog.show()


        }

    }

    override fun setAlarm(time: LocalDateTime, isAlarm: Boolean) {
        val alarmItem = AlarmItem(time = time, isAlarm, message = "test")
        scheduler.scheduleAlarm(alarmItem)
    }

    override fun deleteAlarm() {
        TODO("Not yet implemented")
    }


}