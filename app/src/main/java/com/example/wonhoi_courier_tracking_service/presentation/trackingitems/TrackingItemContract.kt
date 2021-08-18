package com.example.wonhoi_courier_tracking_service.presentation.trackingitems

import com.example.wonhoi_courier_tracking_service.data.entity.TrackingInformation
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingItem
import com.example.wonhoi_courier_tracking_service.presentation.BasePresenter
import com.example.wonhoi_courier_tracking_service.presentation.BaseView

class TrackingItemContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showNoDataDescription()

        fun showTrackingItemInformation(trackingItemInformation : List<Pair<TrackingItem, TrackingInformation >>)

    }

    interface Presenter : BasePresenter {

        var trackingItemInformation : List<Pair<TrackingItem, TrackingInformation>>

        fun refresh()

    }

}