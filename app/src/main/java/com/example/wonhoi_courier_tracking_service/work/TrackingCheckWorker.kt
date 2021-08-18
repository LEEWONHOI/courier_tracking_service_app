package com.example.wonhoi_courier_tracking_service.work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wonhoi_courier_tracking_service.R
import com.example.wonhoi_courier_tracking_service.data.entity.Level
import com.example.wonhoi_courier_tracking_service.data.repository.TrackingItemRepository
import com.example.wonhoi_courier_tracking_service.presentation.MainActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class TrackingCheckWorker(
    val context: Context,
    workerParams : WorkerParameters,
    private val trackingItemRepository : TrackingItemRepository,
    private val dispatcher: CoroutineDispatcher
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(dispatcher) {
        try {
         val startedTrackingItems = trackingItemRepository.getTrackingItemInformation()
             .filter {
                 it.second.level == Level.START
             }

            if (startedTrackingItems.isNotEmpty()) {
                createNotificationChannelIfNeeded()

                val representativeItem = startedTrackingItems.first()
                NotificationManagerCompat
                    .from(context)
                    .notify(
                        NOTIFICATION_ID,
                        createNotification(
                            "Other than ${representativeItem.second.itemName}(${representativeItem.first.company.name})" +
                                    " ${startedTrackingItems.size - 1} parcels have been shipped."
                        )
                    )
            }
            Result.success()
        } catch (exception : Exception) {
            Result.failure()
        }
    }

    private fun createNotificationChannelIfNeeded() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION

            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }


    private fun createNotification(
        message : String?
    ) : Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_local_shipping_24)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }


    companion object {
        private const val CHANEL_NAME = "Daily Tracking Updates"
        private const val CHANNEL_DESCRIPTION = "It tells you the products that have been shipped every day."
        private const val CHANNEL_ID = "Channel Id"
        private const val NOTIFICATION_ID = 101
    }

}