package com.example.weatherapp.map

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable

import android.view.LayoutInflater
import androidx.core.content.ContextCompat.getString
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DialogLayoutBinding

class MapDialog(
    context: Context,
    private val isFav: Boolean,
    private val addToFav: OnClickAddToFav,
    private val updateHomeLocation: OnClickUpdateHomeLocation
) : Dialog(context) {
    private val binding = DialogLayoutBinding.inflate(LayoutInflater.from(context))

    init {
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        binding.btnYes.setOnClickListener {
            this.dismiss()
            if (isFav) {
                addToFav.addToFav()
            } else {
                updateHomeLocation.updateHomeLocation()
            }
        }
        binding.btnNo.setOnClickListener {
            this.dismiss()
        }
    }

    fun showInfoForMap(lat: Double, long: Double, city: String) {
        binding.dialogTitle.text = getString(
            context,
            if (isFav) R.string.AddToFavText else R.string.UpdateHomeLocationText
        )
        binding.tvCountryName.text = city
        binding.tvLong.text = String.format("%.4f", long)
        binding.tvLat.text = String.format("%.4f", lat)
    }
}