package com.example.weatherapp.favorites

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable

import android.view.LayoutInflater
import androidx.core.content.ContextCompat.getString
import com.example.weatherapp.R
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.databinding.DialogLayoutBinding

class FavoritesDialog(
    context: Context, onClickDeleteFavorite:OnClickDeleteFavorite
) : Dialog(context ,) {
    private val binding = DialogLayoutBinding.inflate(LayoutInflater.from(context))
    private var favData: FavData? = null
    init {
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        binding.btnYes.setOnClickListener {
            this.dismiss()
            onClickDeleteFavorite.deleteFavorite(favData!!)
        }
        binding.btnNo.setOnClickListener {
            this.dismiss()
        }
    }

    fun showInfoForFavorites( favData:FavData) {
        this.favData=favData
        binding.dialogTitle.text = "Delete this item from favorite ? "
        binding.tvCountryName.text = favData.city
        binding.tvLong.text = String.format("%.4f", favData.longitude)
        binding.tvLat.text = String.format("%.4f", favData.latitude)
    }
}