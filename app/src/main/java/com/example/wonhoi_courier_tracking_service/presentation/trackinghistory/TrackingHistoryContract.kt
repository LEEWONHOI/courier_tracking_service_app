package com.example.wonhoi_courier_tracking_service.presentation.trackinghistory

import com.example.wonhoi_courier_tracking_service.data.entity.TrackingInformation
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingItem
import com.example.wonhoi_courier_tracking_service.presentation.BasePresenter
import com.example.wonhoi_courier_tracking_service.presentation.BaseView

class TrackingHistoryContract {

    interface View : BaseView<Presenter> {

        fun hideLoadingIndicator()

        fun showTrackingItemInformation(trackingItem : TrackingItem, trackingInformation: TrackingInformation)

        fun finish()

    }

    interface Presenter : BasePresenter {

        fun refresh()

        fun deleteTrackingItem()

    }
}