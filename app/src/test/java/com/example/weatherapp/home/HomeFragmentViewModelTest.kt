package com.example.weatherapp.home

import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.FakeLocal
import com.example.weatherapp.data.source.remote.FakeRemote
import com.example.weatherapp.data.source.sharedPrefrence.FakeShared
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class HomeFragmentViewModelTest {

    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var remoteDataSource: FakeRemote
    private lateinit var localDataSource: FakeLocal
    private lateinit var sharedPrefSource: FakeShared
    private lateinit var repository: WeatherRepository


    @Before
    fun setUp() {
        remoteDataSource = FakeRemote()
        localDataSource = FakeLocal()
        sharedPrefSource = FakeShared()
        repository = WeatherRepository.getInstance(localDataSource, remoteDataSource, sharedPrefSource)
        viewModel = HomeFragmentViewModel(repository)
    }


    @Test
    fun setLatitude_setValue() {
        viewModel.setLatitude(2.0)
        assertEquals(2.0, viewModel.getLatitude())
    }

    @Test
    fun setLongitude_setValue() {
        viewModel.setLongitude(2.0)
        assertEquals(2.0, viewModel.getLongitude())
    }



}