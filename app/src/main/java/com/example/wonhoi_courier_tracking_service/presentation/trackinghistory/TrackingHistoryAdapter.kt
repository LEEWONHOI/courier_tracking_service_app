package com.example.wonhoi_courier_tracking_service.presentation.trackinghistory

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingDetail
import com.example.wonhoi_courier_tracking_service.databinding.ItemTrackingHistoryBinding

class TrackingHistoryAdapter : RecyclerView.Adapter<TrackingHistoryAdapter.ViewHolder>() {

    var data: List<TrackingDetail> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackingHistoryAdapter.ViewHolder {
        val view = ItemTrackingHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackingHistoryAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(
        private val binding: ItemTrackingHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(info: TrackingDetail) {
            binding.timeStampTextView.text = info.timeString
            binding.stateTextView.text = info.kind
            binding.locationTextView.text = "@${info.where}"
        }
    }
}