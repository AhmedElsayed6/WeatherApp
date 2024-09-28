package com.example.weatherapp.alerts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.source.AlarmItem
import com.example.weatherapp.data.source.FavData
import com.example.weatherapp.databinding.RvItemAlarmBinding
import com.example.weatherapp.databinding.RvItemFavBinding
import java.time.format.DateTimeFormatter


class AlarmRecyclerViewAdapter(
    private var data: List<AlarmItem>,
    private val deleteAlarm: OnClickDeleteAlarm
) :
    RecyclerView.Adapter<AlarmRecyclerViewAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = RvItemAlarmBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        with(holder.binding) {
            tvAlarmType.text=if(item.isAlarm) "Alarm" else "Notification"
            tvAlarmDateTime.text=item.time.format(DateTimeFormatter.ofPattern("yy dd MM HH:mm"))
            animationAlarm.setAnimation(if(item.isAlarm) "bluealarmanimation.json" else "blueclockanimation.json")
            btnDelete.setOnClickListener{
                ConfirmationDialog(context,item,deleteAlarm).show()
            }
        }
    }

    inner class ViewHolder(val binding: RvItemAlarmBinding) : RecyclerView.ViewHolder(binding.root)


}