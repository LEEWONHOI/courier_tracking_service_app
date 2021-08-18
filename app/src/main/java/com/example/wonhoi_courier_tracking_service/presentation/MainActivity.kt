package com.example.wonhoi_courier_tracking_service.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.wonhoi_courier_tracking_service.R
import com.example.wonhoi_courier_tracking_service.databinding.ActivityMainBinding
import com.example.wonhoi_courier_tracking_service.work.TrackingCheckWorker
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initWorker()
    }

    private fun initView() {
        val navigationController =
            (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
        // setupWithNavController 를 사용해서 androidx toolbar 에 navi 에 등록한 label 이 자동으로 등록됨
        binding.toolbar.setupWithNavController(navigationController)
    }

    private fun initWorker() {
        val workerStationTime = Calendar.getInstance()
        workerStationTime.set(Calendar.HOUR_OF_DAY, 16)
        val initialDelay = workerStationTime.timeInMillis - System.currentTimeMillis()
        val dailyTrackingCheckRequest =
            PeriodicWorkRequestBuilder<TrackingCheckWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork( // 중복 작동 방지
                "DailyTrackingCheck",
                ExistingPeriodicWorkPolicy.KEEP,    // 기존 work 유지
                dailyTrackingCheckRequest   // 위에서 만든 Request 전달
            )
    }
}