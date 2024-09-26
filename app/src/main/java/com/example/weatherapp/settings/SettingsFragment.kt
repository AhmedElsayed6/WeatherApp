package com.example.weatherapp.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.AppDatabase
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.network.API
import com.example.weatherapp.util.WeatherViewModelFactory


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsFragmentViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(
                    AppDatabase.getInstance(requireContext()).weatherDao()
                ),
                WeatherRemoteDataSource.getInstance(API.retrofitService),
                WeatherSharedPreferenceDataSource.getInstance(requireContext())
            )
        )
        ViewModelProvider(this, factory)[SettingsFragmentViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            if (binding.radioGroupLocation.checkedRadioButtonId != -1) {
                viewModel.setLocationSettings(view.findViewById<RadioButton>(binding.radioGroupLocation.checkedRadioButtonId).text.toString())
            }
            if (binding.radioGroupNotifications.checkedRadioButtonId != -1) {

                viewModel.setNotificationSettings(view.findViewById<RadioButton>(binding.radioGroupNotifications.checkedRadioButtonId).text.toString())
            }
            if (binding.radioGroupLanguage.checkedRadioButtonId != -1) {
                viewModel.setLanguage(view.findViewById<RadioButton>(binding.radioGroupLanguage.checkedRadioButtonId).text.toString())
            }
            if (binding.radioGroupTempUnits.checkedRadioButtonId != -1) {
                viewModel.setLanguage(view.findViewById<RadioButton>(binding.radioGroupTempUnits.checkedRadioButtonId).text.toString())
            }
            if (binding.radioGroupSpeedUnits.checkedRadioButtonId != -1) {
                viewModel.setLanguage(view.findViewById<RadioButton>(binding.radioGroupSpeedUnits.checkedRadioButtonId).text.toString())
            }
        }
    }

}