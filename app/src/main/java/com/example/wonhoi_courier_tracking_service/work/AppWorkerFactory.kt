package com.example.wonhoi_courier_tracking_service.work

import androidx.work.DelegatingWorkerFactory
import com.example.wonhoi_courier_tracking_service.data.repository.TrackingItemRepository
import kotlinx.coroutines.CoroutineDispatcher

class AppWorkerFactory(
    trackingItemRepository: TrackingItemRepository,
    dispatcher: CoroutineDispatcher
) : DelegatingWorkerFactory() {

    init {
        addFactory(TrackingCheckWorkerFactory(trackingItemRepository, dispatcher))
    }

}