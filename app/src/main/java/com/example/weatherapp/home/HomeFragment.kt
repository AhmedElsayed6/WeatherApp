package com.example.weatherapp.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.MainActivity
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.network.API
import com.example.weatherapp.network.WeatherState
import com.example.weatherapp.util.WeatherViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel: HomeFragmentViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource(),
                WeatherRemoteDataSource.getInstance(API.retrofitService)
            )
        )
        ViewModelProvider(this, factory)[HomeFragmentViewModel::class.java]
    }
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentWeather.collect {
                    when (it) {
                        is WeatherState.Error -> {
                            Log.i("here", "onViewCreated: " + it.message)
                        }

                        is WeatherState.Loading -> {
                            Log.i("here", "onViewCreated: " + it.toString())
                        }

                        is WeatherState.Success -> {
                            Log.i("here", "onViewCreated: " + it.weatherData.weather)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Home"
    }


}