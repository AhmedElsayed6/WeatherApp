package com.example.weatherapp.data.source

import com.example.weatherapp.data.source.local.FakeLocal
import com.example.weatherapp.data.source.local.FavoritesState
import com.example.weatherapp.data.source.remote.FakeRemote
import com.example.weatherapp.data.source.sharedPrefrence.FakeShared
import com.example.weatherapp.home.HomeFragmentViewModel
import com.example.weatherapp.network.WeatherState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test


class WeatherRepositoryTest {

    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var remoteDataSource: FakeRemote
    private lateinit var localDataSource: FakeLocal
    private lateinit var sharedPrefSource: FakeShared
    private lateinit var repository: WeatherRepository
    private val fakeWeatherData by lazy {
        WeatherData(
            Coord(0.0, 0.0), listOf(),
            Main(0.0, 0L, 0L, 0.0, 0.0),
            Wind(0.0),
            Clouds(0), 0L, " "
        )
    }

    @Before
    fun setUp() {
        remoteDataSource = FakeRemote()
        localDataSource = FakeLocal()
        sharedPrefSource = FakeShared()
        repository =
            WeatherRepository.getInstance(localDataSource, remoteDataSource, sharedPrefSource)
        viewModel = HomeFragmentViewModel(repository)

    }


    @Test
    fun getCurrentWeather_returnsSuccessWhenResponseIsSuccessful() = runBlockingTest {
        remoteDataSource.weatherData = fakeWeatherData
        remoteDataSource.shouldReturnSuccessfulResponse = true

        // When
        val weatherState = repository.getCurrentWeather(0.0, 0.0)

        // Then
        weatherState.collect { state ->
            assertTrue(state is WeatherState.Success)
        }

    }

    @Test
    fun getCurrentWeather_returnsSuccessWhenResponseIsUnSuccessful() = runBlockingTest {
        remoteDataSource.weatherData = fakeWeatherData
        remoteDataSource.shouldReturnSuccessfulResponse = false

        // When
        val weatherState = repository.getCurrentWeather(0.0, 0.0)

        // Then
        weatherState.collect { state ->
            assertTrue(state is WeatherState.Error)
        }
    }


    @Test
    fun getAllFavorites_returnsSuccessWithFavorites() = runBlockingTest {
        // Given
        localDataSource.addToFav("Favorite 1", 0.0, 0.0)
        // When
        val favoritesState = repository.getAllFavorites()
        // Then
        favoritesState.collect { state ->
            assertTrue(state is FavoritesState.Success)
        }
    }

    @Test
    fun getAllFavorites_returnsSuccessWithEmptyList() = runBlockingTest {
        // When
        val favoritesState = repository.getAllFavorites()
        // Then
        favoritesState.collect { state ->
            assertTrue(state is FavoritesState.Success)
        }
    }


    @Test
    fun setFirstTime_updatesSharedPreferences() = runBlockingTest {
        // When
        repository.setFirstTime()
        // Then
        assertEquals(false, repository.getFirstTime())
    }


}




