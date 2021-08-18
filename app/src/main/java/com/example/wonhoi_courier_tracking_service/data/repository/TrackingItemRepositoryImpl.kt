package com.example.wonhoi_courier_tracking_service.data.repository

import com.example.wonhoi_courier_tracking_service.data.api.SweetTrackerApi
import com.example.wonhoi_courier_tracking_service.data.db.TrackingItemDao
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingInformation
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import java.lang.RuntimeException


class TrackingItemRepositoryImpl(
    private val trackerApi: SweetTrackerApi,
    private val trackingItemDao: TrackingItemDao,
    private val dispatcher: CoroutineDispatcher
) : TrackingItemRepository {

    override val trackingItems: Flow<List<TrackingItem>> =
        trackingItemDao.allTrackingItems()
            .distinctUntilChanged()   // Flow 에서 값을 호출? 관찰할때 .distinctUntilChanged() 는 반복 호출을 방지하기 위해 꼭 넣어주기

    override suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>> =
        withContext(dispatcher) {
            trackingItemDao.getAll()
                .mapNotNull { trackingItem ->   // 아이템이 몇개든 로테이션으로 계속 호출
                    val relatedTrackingInfo = trackerApi.getTrackingInformation(
                        trackingItem.company.code,
                        trackingItem.invoice
                    ).body()

                    if (relatedTrackingInfo?.invoiceNo.isNullOrBlank()) {
                        null
                    } else {
                        trackingItem to relatedTrackingInfo!!       // Pair 로 반환 Pair<TrackingItem, TrackingInformation>
                    }
                }
                .sortedWith(
                    compareBy( // 비교 후 낮은 값부터 정렬함
                        {
                            it.second.level // 배송 진행을 기준으로 먼저 sort 해주고
                        },
                        {
                            -(it.second.lastDetail?.time
                                ?: Long.MAX_VALUE) // 시간 순으로 ( 업데이트가 최근인걸로 )
                        }
                    )
                )
        }

    override suspend fun getTrackingInformation(
        companyCode: String, invoice: String
    ): TrackingInformation? =
        trackerApi.getTrackingInformation(companyCode, invoice)
            .body()
            ?.sortTrackingDetailsByTimeDescending()

    override suspend fun saveTrackingItem(trackingItem: TrackingItem) = withContext(dispatcher) {
        val trackingInformation = trackerApi.getTrackingInformation(
            trackingItem.company.code,
            trackingItem.invoice
        ).body()

        if (!trackingInformation!!.errorMessage.isNullOrBlank()) { // 에러메시지가 null 이나 비어있으면 (문제없이 값을 받았으면) true
            throw RuntimeException(trackingInformation.errorMessage)
        }

        trackingItemDao.insert(trackingItem)
    }

    override suspend fun deleteTrackingItem(trackingItem: TrackingItem) {
        trackingItemDao.delete(trackingItem)
    }

    private fun TrackingInformation.sortTrackingDetailsByTimeDescending()=
        copy(trackingDetails = trackingDetails?.sortedByDescending {
            it.time ?: 0L
        })
}