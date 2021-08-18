package com.example.wonhoi_courier_tracking_service.presentation.addtrackingitem

import com.example.wonhoi_courier_tracking_service.data.entity.ShippingCompany
import com.example.wonhoi_courier_tracking_service.presentation.BasePresenter
import com.example.wonhoi_courier_tracking_service.presentation.BaseView

class AddTrackingItemContract {

    interface View : BaseView<Presenter> {

    fun showShippingCompaniesLoadingIndicator()

    fun hideShippingCompaniesLoadingIndicator()

    fun showSaveTrackingItemIndicator()

    fun hideSaveTrackingItemIndicator()

    fun showRecommendCompanyLoadingIndicator()

    fun hideRecommendCompanyLoadingIndicator()

    fun showCompanies(companies : List<ShippingCompany>)

    fun showRecommendCompany(company : ShippingCompany)

    fun enableSaveButton()

    fun disableSaveButton()

    fun showErrorToast(message:String)

    fun finish()

    }

    interface Presenter : BasePresenter {

        var invoice : String?
        var shippingCompanies : List<ShippingCompany>?
        var selectedShippingCompany : ShippingCompany?

        fun fetchShippingCompanies()

        fun fetchRecommendShippingCompany()

        fun changeSelectedShippingCompany(companyName : String)

        fun changeShippingInvoice(invoice : String)

        fun saveTrackingItem()

    }

}