package com.example.weatherapp.alerts

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.AppDatabase
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.databinding.FragmentAlertsBinding
import com.example.weatherapp.network.API
import com.example.weatherapp.util.WeatherViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDateTime

const val REQUEST_POST_NOTIFICATION = 1001

class AlertsFragment : Fragment(), OnClickSetAlarm, OnClickDeleteAlarm {

    private lateinit var binding: FragmentAlertsBinding
    private val scheduler by lazy {
        AlarmSchedulerImp(this.requireContext())
    }
    private val viewModel: AlertsFragmentViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(
                    AppDatabase.getInstance(requireContext()).weatherDao(),
                    AppDatabase.getInstance(requireContext()).alarmDao()
                ),
                WeatherRemoteDataSource.getInstance(API.retrofitService),
                WeatherSharedPreferenceDataSource.getInstance(this.requireContext())
            )
        )
        ViewModelProvider(this, factory)[AlertsFragmentViewModel::class.java]
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this.requireContext(),
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        context,
                        "Please allow notifications to use this feature",
                        Toast.LENGTH_SHORT
                    ).show()
                    requestPermissions(
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                        REQUEST_POST_NOTIFICATION // Custom request code
                    )
                } else {
                    if (!Settings.canDrawOverlays(context)) {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:${context?.packageName}")
                        )
                        context?.startActivity(intent)
                    } else {
                        val alertDialog = AlertDialog(this.requireContext(), this)
                        alertDialog.show()
                    }
                }
            } else {
                val alertDialog = AlertDialog(this.requireContext(), this)
                alertDialog.show()
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.alarmState.collect {
                    when (it) {
                        is AlarmState.Error -> {
                            Log.i("here", "onViewCreated: " + it.message)
                        }

                        is AlarmState.Loading -> {
                            // binding.pb.visibility = View.VISIBLE
                            Log.i("here", "onViewCreated: loading ")
                        }

                        is AlarmState.Success -> {
                            val managerFav = LinearLayoutManager(requireContext())
                            managerFav.setOrientation(RecyclerView.VERTICAL)
                            binding.rvAlerts.layoutManager = managerFav
                            binding.rvAlerts.adapter =
                                AlarmRecyclerViewAdapter(it.alarmData, this@AlertsFragment)
                        }
                    }
                }
            }
        }

    }


    override fun setAlarm(time: LocalDateTime, isAlarm: Boolean) {
        val alarmItem = AlarmItem(time = time, isAlarm, message = "test")
        scheduler.scheduleAlarm(alarmItem)
        viewModel.addAlarm(alarmItem)
    }

    override fun deleteAlarm(alarmItem: AlarmItem) {
        viewModel.deleteAlarm(alarmItem)
        scheduler.cancelAlarm(alarmItem)
    }


    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.Alerts)
    }


}