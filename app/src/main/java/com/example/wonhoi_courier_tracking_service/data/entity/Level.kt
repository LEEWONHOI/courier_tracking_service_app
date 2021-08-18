package com.example.wonhoi_courier_tracking_service.data.entity

import com.google.gson.annotations.SerializedName

enum class Level(val label: String) {

    @SerializedName("1")
    PREPARE("Preparing"),

    @SerializedName("2")
    READY_FOR_SHIPPING("Collection completed"),

    @SerializedName("3")
    ON_TRANSIT("In transit"),

    @SerializedName("4")
    ON_ARRIVE_ROUTER("Arrival at the branch"),

    @SerializedName("5")
    START("Shipped"),

    @SerializedName("6")
    COMPLETE("Delivered")
}