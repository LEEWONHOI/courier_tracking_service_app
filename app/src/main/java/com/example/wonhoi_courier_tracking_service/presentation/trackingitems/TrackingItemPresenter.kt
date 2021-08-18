package com.example.wonhoi_courier_tracking_service.presentation.trackingitems

import com.example.wonhoi_courier_tracking_service.data.entity.TrackingInformation
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingItem
import com.example.wonhoi_courier_tracking_service.data.repository.TrackingItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class TrackingItemPresenter(
    private val view: TrackingItemContract.View,
    private val trackingItemRepository: TrackingItemRepository
) : TrackingItemContract.Presenter {

    override var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>> =
        emptyList()

    override val scope: CoroutineScope = MainScope()

    init {
        trackingItemRepository
            .trackingItems  // Flow 형태로 변경이 있을때마다 호출됨
            .onEach {
                refresh() // 변경이 있을때 마다 refresh() 함수 실행
            }
            .launchIn(scope)
    }

    override fun onViewCreated() {
        fetchTrackingInformation()
    }

    override fun onDestroyView() {}

    override fun refresh() {
        fetchTrackingInformation(true)
    }

    private fun fetchTrackingInformation(forceFetch: Boolean = false) = scope.launch {
        try {
            view.showLoadingIndicator()

            if (trackingItemInformation.isEmpty() || forceFetch) {
                trackingItemInformation = trackingItemRepository.getTrackingItemInformation()
            }

            if (trackingItemInformation.isEmpty()) {
                view.showNoDataDescription()
            } else {
                view.showTrackingItemInformation(trackingItemInformation)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        } finally {
            view.hideLoadingIndicator()
        }
    }

}