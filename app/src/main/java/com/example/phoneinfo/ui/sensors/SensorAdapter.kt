package com.example.phoneinfo.ui.sensors

import android.hardware.Sensor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneinfo.databinding.ItemSensorBinding

class SensorAdapter(private val sensorList: List<Sensor>) :
    RecyclerView.Adapter<SensorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSensorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sensor = sensorList[position]
        holder.binding.sensorNameTextView.text = sensor.name
        holder.binding.sensorTypeTextView.text = sensor.stringType
    }

    override fun getItemCount() = sensorList.size

    class ViewHolder(val binding: ItemSensorBinding) : RecyclerView.ViewHolder(binding.root)
}