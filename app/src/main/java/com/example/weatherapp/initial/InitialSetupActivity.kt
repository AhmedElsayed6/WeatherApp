package com.example.weatherapp.initial

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.MainActivity
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.AppDatabase
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.databinding.ActivityInitialSetupBinding
import com.example.weatherapp.network.API
import com.example.weatherapp.settings.SettingsFragmentViewModel
import com.example.weatherapp.util.WeatherViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InitialSetupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInitialSetupBinding
    private val viewModel: InitialSetupViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(AppDatabase.getInstance(this).weatherDao(),
                    AppDatabase.getInstance(this).alarmDao()),
                WeatherRemoteDataSource.getInstance(API.retrofitService),
                WeatherSharedPreferenceDataSource.getInstance(this)
            )
        )
        ViewModelProvider(this, factory)[InitialSetupViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var location: String = ""
        var notification: String = ""
        var language: String = ""

        binding.btnOkay.setOnClickListener {
            if (binding.radioGroupLocation.checkedRadioButtonId != -1) {
                location =
                    findViewById<RadioButton>(binding.radioGroupLocation.checkedRadioButtonId).text.toString()
            }

            if (binding.radioGroupNotifications.checkedRadioButtonId != -1) {
                notification =
                    findViewById<RadioButton>(binding.radioGroupNotifications.checkedRadioButtonId).text.toString()
            }

            if (binding.radioGroupLanguage.checkedRadioButtonId != -1) {
                language =
                    findViewById<RadioButton>(binding.radioGroupLanguage.checkedRadioButtonId).text.toString()
            }

            viewModel.setUpData(location, language, notification)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            delay(1000)
            if (viewModel.isFirstTime()) {
                binding.groupSplashGroup.visibility = android.view.View.GONE
                binding.cvInitialSetup.visibility = android.view.View.VISIBLE
            } else {
                val intent = Intent(this@InitialSetupActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}