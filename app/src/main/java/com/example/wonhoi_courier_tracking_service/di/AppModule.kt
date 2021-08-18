package com.example.wonhoi_courier_tracking_service.di

import android.app.Activity
import com.example.wonhoi_courier_tracking_service.BuildConfig
import com.example.wonhoi_courier_tracking_service.data.api.SweetTrackerApi
import com.example.wonhoi_courier_tracking_service.data.api.Url
import com.example.wonhoi_courier_tracking_service.data.db.AppDatabase
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingInformation
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingItem
import com.example.wonhoi_courier_tracking_service.data.preference.PreferenceManager
import com.example.wonhoi_courier_tracking_service.data.preference.SharedPreferenceManager
import com.example.wonhoi_courier_tracking_service.data.repository.*
import com.example.wonhoi_courier_tracking_service.presentation.addtrackingitem.AddTrackingItemContract
import com.example.wonhoi_courier_tracking_service.presentation.addtrackingitem.AddTrackingItemFragment
import com.example.wonhoi_courier_tracking_service.presentation.addtrackingitem.AddTrackingItemPresenter
import com.example.wonhoi_courier_tracking_service.presentation.trackinghistory.TrackingHistoryContract
import com.example.wonhoi_courier_tracking_service.presentation.trackinghistory.TrackingHistoryFragment
import com.example.wonhoi_courier_tracking_service.presentation.trackinghistory.TrackingHistoryPresenter
import com.example.wonhoi_courier_tracking_service.presentation.trackingitems.TrackingItemContract
import com.example.wonhoi_courier_tracking_service.presentation.trackingitems.TrackingItemPresenter
import com.example.wonhoi_courier_tracking_service.presentation.trackingitems.TrackingItemsFragment
import com.example.wonhoi_courier_tracking_service.work.AppWorkerFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().trackingItemDao() }
    single { get<AppDatabase>().shippingCompanyDao() }

    // Api
    single {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
    single<SweetTrackerApi> {
        Retrofit.Builder().baseUrl(Url.SWEET_TRACKER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Repository
    single<TrackingItemRepository> { TrackingItemRepositoryImpl(get(),get(),get()) }
    // single<TrackingItemRepository> { TrackingItemRepositoryStub() } // noti 테스트 할려면 setInitialDelay 의 init 값을 0으로 변경하면 됨
    single<ShippingCompanyRepository> { ShippingCompanyRepositorylmpl(get(), get(), get(), get()) }

    // Work
    single { AppWorkerFactory(get(), get()) }

    // Presentation
    scope<TrackingItemsFragment> {
        scoped<TrackingItemContract.Presenter> { TrackingItemPresenter(getSource(), get()) }
    }

    scope<AddTrackingItemFragment> {
        scoped<AddTrackingItemContract.Presenter> { AddTrackingItemPresenter(getSource(), get(), get()) }
    }

    scope<TrackingHistoryFragment> {
        scoped<TrackingHistoryContract.Presenter> { (trackingItem : TrackingItem, trackingInformation : TrackingInformation) ->
            TrackingHistoryPresenter(getSource(), get(), trackingItem, trackingInformation)
        }
    }

}