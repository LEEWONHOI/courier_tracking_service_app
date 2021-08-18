package com.example.wonhoi_courier_tracking_service.presentation.trackingitems

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wonhoi_courier_tracking_service.R
import com.example.wonhoi_courier_tracking_service.data.entity.Level
import com.example.wonhoi_courier_tracking_service.data.entity.ShippingCompany
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingInformation
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingItem
import com.example.wonhoi_courier_tracking_service.databinding.ItemTrackingBinding
import com.example.wonhoi_courier_tracking_service.extension.setTextColorRes
import com.example.wonhoi_courier_tracking_service.extension.toReadableDateString
import java.util.*

class TrackingItemsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data : List<Pair<TrackingItem, TrackingInformation>> = emptyList()
    var onClickItemListener : ((TrackingItem, TrackingInformation) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTrackingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (trackingItem, trackingInformation) = data[position]    // Pair 타입

        (holder as ViewHolder).bind(trackingItem.company, trackingInformation)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding : ItemTrackingBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                data.getOrNull(adapterPosition)?.let {  (item, information) ->  //Pair<TrackingItem, TrackingInformation>
                    onClickItemListener?.invoke(item,information)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(company : ShippingCompany, information: TrackingInformation) {
            binding.updatedAtTextView.text =
                Date(information.lastDetail?.time ?: System.currentTimeMillis()).toReadableDateString()

            binding.levelLabelTextView.text = information.level?.label
            when(information.level) {
                Level.COMPLETE -> {
                    binding.levelLabelTextView.setTextColor(R.attr.colorPrimary)
                    binding.root.alpha = 0.5f
                }
                Level.PREPARE -> {
                    binding.levelLabelTextView.setTextColorRes(R.color.orange)
                    binding.root.alpha = 1f
                }
                else -> {
                    binding.levelLabelTextView.setTextColorRes(R.color.green)
                    binding.root.alpha = 1f
                }
            }
            binding.invoiceTextView.text = information.invoiceNo

            if (information.itemName.isNullOrBlank()) {
                binding.itemNameTextView.text = "no item name"
                binding.itemNameTextView.setTextColorRes(R.color.green)
            } else {
                binding.itemNameTextView.text = information.itemName
                binding.itemNameTextView.setTextColorRes(R.color.black)
            }

            binding.lastStateTextView.text = information.lastDetail?.let {
                it.kind + " @${it.where}"
            }
            binding.companyNameTextView.text = company.name
        }
    }
}