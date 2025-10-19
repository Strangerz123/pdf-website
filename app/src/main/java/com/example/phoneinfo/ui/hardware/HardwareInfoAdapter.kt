package com.example.phoneinfo.ui.hardware

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneinfo.databinding.ItemHardwareInfoBinding

class HardwareInfoAdapter(private val hardwareInfoList: List<HardwareInfo>) :
    RecyclerView.Adapter<HardwareInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHardwareInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hardwareInfo = hardwareInfoList[position]
        holder.binding.keyTextView.text = hardwareInfo.key
        holder.binding.valueTextView.text = hardwareInfo.value
    }

    override fun getItemCount() = hardwareInfoList.size

    class ViewHolder(val binding: ItemHardwareInfoBinding) : RecyclerView.ViewHolder(binding.root)
}