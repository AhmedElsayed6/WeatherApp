package com.example.weatherapp.settings

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.LocaleList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.AppDatabase
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.map.MapActivity
import com.example.weatherapp.network.API
import com.example.weatherapp.util.WeatherViewModelFactory
import com.example.weatherapp.util.isNetworkAvailable
import java.util.Locale


class SettingsFragment : Fragment() {
    private var windSpeedUnit: String = ""
    private var tempUnit: String = ""
    private var languageSettings: String = ""
    private var locationSettings: String = ""
    private var notificationSettings: String = ""
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var view: View
    private val viewModel: SettingsFragmentViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(
                    AppDatabase.getInstance(requireContext()).weatherDao(),
                    AppDatabase.getInstance(requireContext()).alarmDao()
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
        this.view = view
        initUnits()
        binding.rbMap.setOnClickListener {
            if (isNetworkAvailable(this.requireContext())) {
                val intent = Intent(requireActivity(), MapActivity::class.java)
                intent.putExtra("fav", false)
                startActivity(intent)
            }
            else{
                Toast.makeText(this@SettingsFragment.requireActivity(),"Can't open map without internet", Toast.LENGTH_SHORT).show()
            }

        }
        binding.rbEnglish.setOnClickListener {
            changLanguage("en")
            viewModel.setLanguage("English")
        }
        binding.rbArabic.setOnClickListener {
            changLanguage("ar")
            viewModel.setLanguage("Arabic")
        }
        binding.btnSave.setOnClickListener {
            if (binding.radioGroupLocation.checkedRadioButtonId != -1) {
                when (view.findViewById<RadioButton>(binding.radioGroupLocation.checkedRadioButtonId).text.toString()) {
                    "GPS" -> viewModel.setLocationSettings("GPS")
                    "Map" -> viewModel.setLocationSettings("Map")
                }
            }
            if (binding.radioGroupNotifications.checkedRadioButtonId != -1) {
                viewModel.setNotificationSettings(view.findViewById<RadioButton>(binding.radioGroupNotifications.checkedRadioButtonId).text.toString())
            }
            if (binding.radioGroupLanguage.checkedRadioButtonId != -1) {
                languageSettings =
                    view.findViewById<RadioButton>(binding.radioGroupLanguage.checkedRadioButtonId).text.toString()
                when (languageSettings) {
                    getString(R.string.English) -> viewModel.setLanguage("English")
                    getString(R.string.Arabic) -> viewModel.setLanguage("Arabic")
                }
            }
            if (binding.radioGroupTempUnits.checkedRadioButtonId != -1) {
                //localcise()
                when (view.findViewById<RadioButton>(binding.radioGroupTempUnits.checkedRadioButtonId).text.toString()) {
                    getString(R.string.Celsius) -> viewModel.setUnitTemp("C")
                    getString(R.string.Kelvin) -> viewModel.setUnitTemp("K")
                    getString(R.string.Fahrenheit) -> viewModel.setUnitTemp("F")
                }
            }
            if (binding.radioGroupSpeedUnits.checkedRadioButtonId != -1) {
                when (view.findViewById<RadioButton>(binding.radioGroupSpeedUnits.checkedRadioButtonId).text.toString()) {
                    getString(R.string.MeterSec) -> viewModel.setUnitWind("ms")
                    getString(R.string.MileHour) -> viewModel.setUnitWind("mh")
                }
            }


        }
    }

    fun changLanguage(code: String) {
        val local = Locale(code)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocales(LocaleList(local, Locale.US))
        config.setLayoutDirection(Locale.US)
        resources.updateConfiguration(config, resources.displayMetrics)
        requireActivity().recreate()
    }

    private fun initUnits() {

        locationSettings = viewModel.getLocationSettings()
        languageSettings = viewModel.getLanguageSettings()
        notificationSettings = viewModel.getNotificationSettings()
        windSpeedUnit = viewModel.getUnitWind()
        tempUnit = viewModel.getUnitTemp()

        binding.radioGroupLocation.check(if (locationSettings == "GPS") R.id.rbGPS else R.id.rbMap)
        binding.radioGroupLanguage.check(if (languageSettings == "English") R.id.rbEnglish else R.id.rbArabic)
        binding.radioGroupNotifications.check(if (notificationSettings == "Enable") R.id.rbEnable else R.id.rbDisable)
        binding.radioGroupTempUnits.check(if (tempUnit == "C") R.id.rbC else if (tempUnit == "K") R.id.rbK else R.id.rbF)
        binding.radioGroupSpeedUnits.check(if (windSpeedUnit == "ms") R.id.rbMS else R.id.rbMH)
    }


    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.title = "Settings"
    }

}