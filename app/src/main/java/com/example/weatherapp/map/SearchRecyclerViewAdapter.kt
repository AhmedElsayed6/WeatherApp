package com.example.weatherapp.map


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.RvItemSearchBinding


class SearchRecyclerViewAdapter(
    private var data: List<String>,
    private val addToFavorite: OnSearchItemClick
) :
    RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = RvItemSearchBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        with(holder.binding) {
            text1.text = item
            text1.setOnClickListener(){
                addToFavorite.locationLookUp(item)
            }
        }
    }

    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RvItemSearchBinding) : RecyclerView.ViewHolder(binding.root)


}