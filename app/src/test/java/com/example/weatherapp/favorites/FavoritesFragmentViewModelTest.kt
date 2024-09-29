package com.example.weatherapp.favorites

import com.example.weatherapp.data.source.FakeRepo
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.data.source.local.FavoritesState
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test


class FavoritesFragmentViewModelTest {

    private lateinit var viewModel: FavoritesFragmentViewModel
    private lateinit var fakeRepo: FakeRepo

    @Before
    fun setUp() {
        fakeRepo = FakeRepo()
        viewModel = FavoritesFragmentViewModel(fakeRepo)
    }


    @Test
    fun getAllFavorites_FavoriteStateSuccessWhenFavoritesExist() = runBlockingTest {
        fakeRepo.addToFav("Alex", 40.7, -74.0)
        val job = launch {
            viewModel.getAllFavorites()
        }.join()
        var state: FavoritesState? = null
        val collectionJob = launch {
            viewModel.favoritesState.collect { value ->
                state = value
            }
        }
        delay(100)
        assertTrue(state is FavoritesState.Success)
        collectionJob.cancel()
    }

    @Test
    fun deleteFavorite_FavoriteStateErrorWhenNoData() = runBlockingTest {

        fakeRepo.addToFav("Alex", 40.7, -74.0)
        fakeRepo.deleteFavoriteData(FavData("Alex", 40.7, -74.0))
        val job = launch {
            viewModel.getAllFavorites()
        }.join()
        var state: FavoritesState? = null
        val collectionJob = launch {
            viewModel.favoritesState.collect { value ->
                state = value
            }
        }
        delay(100)
        collectionJob.cancel()
        assertTrue(state is FavoritesState.Error)

    }
}