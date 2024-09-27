package com.example.weatherapp.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.source.WeatherData
import com.example.weatherapp.databinding.RvItemDailyBinding
import com.example.weatherapp.util.toDaysTime
import com.example.weatherapp.util.toDrawable
import com.example.weatherapp.util.toFahrenheit
import com.example.weatherapp.util.toKelvin
import com.example.weatherapp.util.toTwoDecimalPlaces


class DailyRecyclerViewAdapter(private var data: List<WeatherData>, private var tempUnit: String) :
    RecyclerView.Adapter<DailyRecyclerViewAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = RvItemDailyBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        with(holder.binding) {
            if (position == 0)
                tvDayName.text = "Tomorrow"
            else
                tvDayName.text = item.dt.toDaysTime()
            when (tempUnit) {
                "C" ->   tvDayWeatherTemp.text =
                    item.main.temp_min.toTwoDecimalPlaces().toString() + " $tempUnit° / " + item.main.temp_max.toString() + "   $tempUnit°"
                "K" ->   tvDayWeatherTemp.text =
                    item.main.temp_min.toKelvin().toTwoDecimalPlaces().toString() + " $tempUnit° / " + item.main.temp_max.toKelvin().toTwoDecimalPlaces().toString() + "   $tempUnit°"

                "F" ->   tvDayWeatherTemp.text =
                    item.main.temp_min.toFahrenheit().toTwoDecimalPlaces().toString() + " $tempUnit° / " + item.main.temp_max.toFahrenheit().toTwoDecimalPlaces().toString() + "   $tempUnit°"
            }

            ivDayWeatherIcon.setImageResource(item.weather[0].icon.toDrawable())

            tvDayWeatherDesc.text = item.weather[0].description.replaceFirstChar { it.uppercaseChar() }

        }
    }

    fun setData(data: List<WeatherData>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RvItemDailyBinding) : RecyclerView.ViewHolder(binding.root)


}