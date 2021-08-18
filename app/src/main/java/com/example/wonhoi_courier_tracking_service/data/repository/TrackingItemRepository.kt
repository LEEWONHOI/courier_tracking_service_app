package com.example.wonhoi_courier_tracking_service.data.repository

import com.example.wonhoi_courier_tracking_service.data.entity.TrackingInformation
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingItem
import kotlinx.coroutines.flow.Flow

interface TrackingItemRepository {

    val trackingItems : Flow<List<TrackingItem>>

    suspend fun getTrackingItemInformation() : List<Pair<TrackingItem, TrackingInformation>>

    suspend fun getTrackingInformation(companyCode : String, invoice : String) : TrackingInformation?

    suspend fun saveTrackingItem(trackingItem: TrackingItem)

    suspend fun deleteTrackingItem(trackingItem: TrackingItem)

}