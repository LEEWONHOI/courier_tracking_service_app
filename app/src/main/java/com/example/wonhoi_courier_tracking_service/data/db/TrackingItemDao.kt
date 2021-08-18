package com.example.wonhoi_courier_tracking_service.data.db

import androidx.room.*
import com.example.wonhoi_courier_tracking_service.data.entity.TrackingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingItemDao {

    @Query("SELECT * FROM TrackingItem")
    fun allTrackingItems() : Flow<List<TrackingItem>>

    @Query("SELECT * FROM TrackingItem")
    suspend fun getAll() : List<TrackingItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TrackingItem)

    @Delete
    suspend fun delete(item:TrackingItem)

}