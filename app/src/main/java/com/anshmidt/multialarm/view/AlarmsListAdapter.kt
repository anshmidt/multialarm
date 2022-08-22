package com.anshmidt.multialarm.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anshmidt.multialarm.databinding.ItemAlarmBinding
import org.threeten.bp.LocalTime

class AlarmsListAdapter : RecyclerView.Adapter<AlarmsListAdapter.ViewHolder>() {

    var items: List<LocalTime> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocalTime) {
            binding.item = item
        }
    }
}