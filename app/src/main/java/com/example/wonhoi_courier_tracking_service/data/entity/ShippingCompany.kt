package com.example.wonhoi_courier_tracking_service.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ShippingCompany( // api 문서 참조
    @PrimaryKey
    @SerializedName("Code")
    val code: String,
    @SerializedName("Name")
    val name: String
) : Parcelable