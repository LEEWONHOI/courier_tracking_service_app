package com.example.wonhoi_courier_tracking_service.data.repository

import com.example.wonhoi_courier_tracking_service.data.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random
import kotlin.random.nextLong
// for test
class TrackingItemRepositoryStub : TrackingItemRepository {

//    override suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>> =
//        (1_000_000..1_000_020)
//            .map {
//                it.toString()   // EXAMPLE NUMBER
//            }
//            .map { invoice ->
//                val currentTimeMillis = System.currentTimeMillis()
//                TrackingItem(invoice, ShippingCompany("1", "Delivery company")) to
//                        TrackingInformation(
//                            invoiceNo = invoice,
//                            itemName = if (Random.nextBoolean()) "이름 있음" else null,
//                            level = Level.values().random(),
//                            lastDetail = TrackingDetail(
//                                kind = "Arrival",
//                                where = "Yeoksam Station",
//                                time = Random.nextLong(
//                                    currentTimeMillis - 1000L * 60L * 60L * 24L * 20L..currentTimeMillis
//                                )
//                            )
//                        )
//            }
//            .sortedWith(
//                compareBy(
//                    {
//                        it.second.level
//                    },
//                    {
//                        -(it.second.lastDetail?.time ?: Long.MAX_VALUE)
//                    }
//                )
//            )

    override val trackingItems: Flow<List<TrackingItem>> = flowOf(emptyList())

    override suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>> =
        listOf(
            TrackingItem("1", ShippingCompany("1", "American Express")) to TrackingInformation(itemName = "Sneakers", level = Level.START),
            TrackingItem("2", ShippingCompany("1", "American Express")) to TrackingInformation(itemName = "Toy", level = Level.START),
            TrackingItem("3", ShippingCompany("1", "American Express")) to TrackingInformation(itemName = "Clothing", level = Level.START),
            TrackingItem("4", ShippingCompany("1", "American Express")) to TrackingInformation(itemName = "Home Appliances", level = Level.START),
            TrackingItem("5", ShippingCompany("1", "American Express")) to TrackingInformation(itemName = "Album/DVD", level = Level.ON_TRANSIT),
            TrackingItem("6", ShippingCompany("1", "American Express")) to TrackingInformation(itemName = "Books", level = Level.COMPLETE),
        )

    override suspend fun getTrackingInformation(companyCode: String, invoice: String): TrackingInformation? = null

    override suspend fun saveTrackingItem(trackingItem: TrackingItem) = Unit

    override suspend fun deleteTrackingItem(trackingItem: TrackingItem) = Unit

}