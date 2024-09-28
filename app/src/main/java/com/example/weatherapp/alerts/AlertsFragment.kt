package com.example.weatherapp.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.databinding.FragmentAlertsBinding
import java.time.LocalDateTime


class AlertsFragment : Fragment() {

    private lateinit var binding: FragmentAlertsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scheduler = AlarmSchedulerImp(this.requireContext())
        binding.btnAddAlert.setOnClickListener {
            val alarmItem = AlarmItem(time = LocalDateTime.now().plusSeconds(5L), message = "test")
            scheduler.scheduleAlarm(alarmItem )
        }

    }


}