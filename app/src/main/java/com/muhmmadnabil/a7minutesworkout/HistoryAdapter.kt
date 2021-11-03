package com.muhmmadnabil.a7minutesworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.muhmmadnabil.a7minutesworkout.databinding.ItemHistoryRecyclerViewBinding

class HistoryAdapter(private val items: ArrayList<String>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryRecyclerViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = items[position]
        holder.tvPosition.text = (position + 1).toString()
        holder.tvItem.text = date

        if (position % 2 == 0) {
            holder.llHistory.setBackgroundColor(Color.parseColor("#EBEBEB"))
        } else {
            holder.llHistory.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: ItemHistoryRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val llHistory = binding.llHistory
        val tvItem = binding.tvItem
        val tvPosition = binding.tvPosition
    }

}