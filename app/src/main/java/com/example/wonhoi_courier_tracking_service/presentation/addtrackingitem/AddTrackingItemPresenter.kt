package com.example.wonhoi_courier_tracking_service.presentation.addtrackingitem

import com.example.wonhoi_courier_tracking_service.data.entity.ShippingCompany
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingItem
import com.example.wonhoi_courier_tracking_service.data.repository.ShippingCompanyRepository
import com.example.wonhoi_courier_tracking_service.data.repository.TrackingItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AddTrackingItemPresenter(
    private val view : AddTrackingItemContract.View,
    private val shippingCompanyRepository: ShippingCompanyRepository,
    private val trackerRepository : TrackingItemRepository
) : AddTrackingItemContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override var invoice: String? = null
    override var shippingCompanies: List<ShippingCompany>? = null
    override var selectedShippingCompany: ShippingCompany? = null

    override fun onViewCreated() {
        fetchShippingCompanies()
    }

    override fun onDestroyView() { }

    override fun fetchShippingCompanies() {
        scope.launch {
            view.showShippingCompaniesLoadingIndicator()
            if (shippingCompanies.isNullOrEmpty()) {
                shippingCompanies = shippingCompanyRepository.getShippingCompanies()
            }

            shippingCompanies?.let {
                view.showCompanies(it)}
            view.hideShippingCompaniesLoadingIndicator()
        }
    }

    override fun fetchRecommendShippingCompany() {
        scope.launch {
            view.showRecommendCompanyLoadingIndicator()
            shippingCompanyRepository.getRecommendShippingCompany(invoice!!)?.let { recommendCompany ->
                view.showRecommendCompany(recommendCompany)
            }
            view.hideRecommendCompanyLoadingIndicator()

        }
    }

    override fun changeSelectedShippingCompany(companyName: String) {
        selectedShippingCompany = shippingCompanies?.find {
            it.name == companyName
        }
        enableSaveButtonIfAvailable()
    }

    override fun changeShippingInvoice(invoice: String) {
        this.invoice = invoice
        enableSaveButtonIfAvailable()
    }

    override fun saveTrackingItem() {
        scope.launch {
            try {
                view.showSaveTrackingItemIndicator()
                trackerRepository.saveTrackingItem(
                    TrackingItem(
                        invoice!!,
                        selectedShippingCompany!!
                    )
                )
                view.finish()
            } catch (exception : Exception) {
                view.showErrorToast(exception.message ?: "It couldn't add a tracking number because there was a problem with the service.")
            } finally {
                view.hideSaveTrackingItemIndicator()
            }
        }
    }

    private fun enableSaveButtonIfAvailable() {
        if(!invoice.isNullOrBlank() && selectedShippingCompany != null) {
            view.enableSaveButton()
        } else {
            view.disableSaveButton()
        }
    }

}