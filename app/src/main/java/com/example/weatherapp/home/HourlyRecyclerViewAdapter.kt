package com.example.weatherapp.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.source.WeatherData
import com.example.weatherapp.databinding.RvItemHourlyBinding
import com.example.weatherapp.util.toAMPM
import com.example.weatherapp.util.toDrawable
import com.example.weatherapp.util.toDaysTime


class HourlyRecyclerViewAdapter(private var data: List<WeatherData> ,private var tempUnit: String) : RecyclerView.Adapter<HourlyRecyclerViewAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = RvItemHourlyBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        with(holder.binding) {
            ivHourlyImage.setImageResource(item.weather.get(0).icon.toDrawable())
            tvHourlyTime.text=item.dt.toAMPM()
            tvHourlyTemp.text=item.main.temp_min.toString()+" $tempUnit / "+item.main.temp_max.toString()+"  $tempUnit"

        }
    }

    fun setData(data: List<WeatherData>) {
        this.data=data
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RvItemHourlyBinding) : RecyclerView.ViewHolder(binding.root)


}