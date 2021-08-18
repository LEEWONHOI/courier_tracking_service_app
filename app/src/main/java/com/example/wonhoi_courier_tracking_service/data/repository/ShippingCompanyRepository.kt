package com.example.wonhoi_courier_tracking_service.data.repository

import com.example.wonhoi_courier_tracking_service.data.entity.ShippingCompany

interface ShippingCompanyRepository {

    suspend fun getShippingCompanies() : List<ShippingCompany>

    suspend fun getRecommendShippingCompany(invoice : String) : ShippingCompany?

}