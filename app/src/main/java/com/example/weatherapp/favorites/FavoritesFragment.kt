package com.example.weatherapp.favorites

import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.data.source.WeatherRepository
import com.example.weatherapp.data.source.local.AppDatabase
import com.example.weatherapp.data.source.local.FavoritesState
import com.example.weatherapp.data.source.local.WeatherLocalDataSource
import com.example.weatherapp.data.source.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.source.sharedPrefrence.WeatherSharedPreferenceDataSource
import com.example.weatherapp.databinding.FragmentFavoritesBinding
import com.example.weatherapp.map.MapActivity
import com.example.weatherapp.network.API
import com.example.weatherapp.util.WeatherViewModelFactory
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment(), OnClickHandleButton , OnClickDeleteFavorite , OnClickFavoriteDetails {
    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoritesFragmentViewModel by lazy {
        val factory = WeatherViewModelFactory(
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(
                    AppDatabase.getInstance(requireContext()).weatherDao()
                ),
                WeatherRemoteDataSource.getInstance(API.retrofitService),
                WeatherSharedPreferenceDataSource.getInstance(this.requireContext())
            )
        )
        ViewModelProvider(this, factory)[FavoritesFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddToFav.setOnClickListener {
            val intent = Intent(requireActivity(), MapActivity::class.java)
            intent.putExtra("fav", true)
            startActivity(intent)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoritesState.collect {
                    when (it) {
                        is FavoritesState.Error -> {
                            Log.i("here", "onViewCreated: " + it.message)
                        }

                        is FavoritesState.Loading -> {
                            // binding.pb.visibility = View.VISIBLE
                            Log.i("here", "onViewCreated: loading ")
                        }

                        is FavoritesState.Success -> {
                            val managerFav = LinearLayoutManager(requireContext())
                            managerFav.setOrientation(RecyclerView.VERTICAL)
                            binding.rvFavorites.layoutManager = managerFav
                            binding.rvFavorites.adapter =
                                FavRecyclerViewAdapter(it.favoritesData, this@FavoritesFragment, this@FavoritesFragment)
                        }
                    }
                }
            }
        }


    }


    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.title = "Favorites"
    }


    override fun deleteItem(favData: FavData) {
        val dialog =
            FavoritesDialog(this.requireContext() , this)
        dialog.show()
        dialog.showInfoForFavorites( favData )

    }

    override fun deleteFavorite(favData: FavData) {
        viewModel.deleteFavorite(favData)
    }

    override fun goToDetails(favData: FavData) {
        val bundle = Bundle().apply {
            putSerializable("data", favData)
        }
        findNavController().navigate(R.id.favoritesDetailsFragment, bundle)
    }


}