package com.example.weatherapp.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.databinding.RvItemFavBinding


class FavRecyclerViewAdapter(
    private var data: List<FavData>,
    private val onClickDeleteFavorites: OnClickHandleButton,
    private val onClickFavoriteDetails: OnClickFavoriteDetails
) :
    RecyclerView.Adapter<FavRecyclerViewAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = RvItemFavBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        with(holder.binding) {
            tvCountryName.text = item.city
            tvLong.text = String.format("%.4f", item.longitude)
            tvLat.text = String.format("%.4f", item.latitude)
            btnDelete.setOnClickListener {
                onClickDeleteFavorites.deleteItem(item)
            }
            cvFavoriteItem.setOnClickListener {
                onClickFavoriteDetails.goToDetails(item)
            }

        }
    }

    fun setData(data: List<FavData>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RvItemFavBinding) : RecyclerView.ViewHolder(binding.root)


}