package com.example.wonhoi_courier_tracking_service.data.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(primaryKeys = ["invoice", "code"])
data class TrackingItem(    // 저장을 편하게 하기 위한 엔티티 추가
    val invoice : String,
    @Embedded val company: ShippingCompany  //@Embedded 니깐 code name 둘다 사용 가능. 저장시에는 묶여서 저장
) : Parcelable