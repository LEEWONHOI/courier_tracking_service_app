package com.example.wonhoi_courier_tracking_service.data.entity

import com.google.gson.annotations.SerializedName

data class ShippingCompanies(

    @SerializedName("Company", alternate = ["Recommend"])
    val shippingCompanies: List<ShippingCompany>? = null

)