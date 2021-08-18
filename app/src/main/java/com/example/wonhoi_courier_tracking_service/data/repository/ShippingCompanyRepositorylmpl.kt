package com.example.wonhoi_courier_tracking_service.data.repository

import com.example.wonhoi_courier_tracking_service.data.api.SweetTrackerApi
import com.example.wonhoi_courier_tracking_service.data.db.ShippingCompanyDao
import com.example.wonhoi_courier_tracking_service.data.entity.ShippingCompany
import com.example.wonhoi_courier_tracking_service.data.preference.PreferenceManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ShippingCompanyRepositorylmpl(
    private val trackerApi : SweetTrackerApi,
    private val shippingCompanyDao : ShippingCompanyDao,
    private val preferenceManager : PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : ShippingCompanyRepository {

    override suspend fun getShippingCompanies(): List<ShippingCompany> = withContext(dispatcher) {
        val currentTimeMillis = System.currentTimeMillis()
        val lastDatabaseUpdatedTimeMillis = preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        if (lastDatabaseUpdatedTimeMillis == null ||
                CACHE_MAX_AGE_MILLIS < (currentTimeMillis - lastDatabaseUpdatedTimeMillis)
        ) {
            val shippingCompanies = trackerApi.getShippingCompanies()
                .body()
                ?.shippingCompanies
                ?: emptyList()
            shippingCompanyDao.insert(shippingCompanies)
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, currentTimeMillis)
        }

        shippingCompanyDao.getAll()
    }

    override suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany? = withContext(dispatcher) {
        try {
            trackerApi.getRecommendShippingCompanies(invoice)
                .body()
                ?.shippingCompanies
                // 추천 택배사가 여러곳이 나오는데, 그중에 낮은 값일 수록 메이저 택배회사. 그러므로 가장 메이저한 쪽 1곳을 추천한다.
                ?.minByOrNull {
                    it.code.toIntOrNull() ?: Int.MAX_VALUE
                }
        } catch (exception : Exception) {
            null
        }
    }

    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS = "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
        private const val CACHE_MAX_AGE_MILLIS = 1000L * 60 * 60 * 24 * 7
    }

}