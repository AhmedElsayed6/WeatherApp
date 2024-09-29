package com.example.weatherapp.initial

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.LocaleList
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.AppDatabase
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.databinding.ActivityInitialSetupBinding
import com.example.weatherapp.map.MapActivity
import com.example.weatherapp.network.API
import com.example.weatherapp.util.WeatherViewModelFactory
import com.example.weatherapp.util.isNetworkAvailable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class InitialSetupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInitialSetupBinding
    private val viewModel: InitialSetupViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(
                    AppDatabase.getInstance(this).weatherDao(),
                    AppDatabase.getInstance(this).alarmDao()
                ),
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

        binding.rbEnglish.setOnClickListener {
            changLanguage("en")
            viewModel.setLanguage("English")
        }
        binding.rdArabic.setOnClickListener {
            changLanguage("ar")
            viewModel.setLanguage("Arabic")
        }
        binding.rdMap.setOnClickListener {
            if (isNetworkAvailable(this)) {
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra("fav", false)
                startActivity(intent)
            } else {

                Toast.makeText(this, "Can't open map without internet", Toast.LENGTH_SHORT).show()
            }

        }
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

            when (language) {
                getString(R.string.English) -> language = "English"
                getString(R.string.Arabic) -> language = "Arabic"
                else->"English"
            }

            when (location) {
                getString(R.string.GPS) ->location = "GPS"
                getString(R.string.Map)-> location = "Map"
                else->"GPS"
            }

            when (notification) {
                getString(R.string.Enable) -> notification = "Enable"
                getString(R.string.Disable) ->notification = "Disable"
                else -> "Enable"
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


    fun changLanguage(code: String) {
        val local = Locale(code)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocales(LocaleList(local, Locale.US))
        config.setLayoutDirection(Locale.US)
        resources.updateConfiguration(config, resources.displayMetrics)
        this.recreate()
    }
}