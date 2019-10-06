package com.example.showimage.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.showimage.database.model.ImageDBModel
import com.example.showimage.database.model.MarkerDBModel
import io.reactivex.Flowable

@Dao
interface MarkerDAO {
    @Query("SELECT * FROM MARKER WHERE marker.id = :id")
    suspend fun selectMarker(id: Int): MarkerDBModel

    @Query("SELECT * FROM MARKER WHERE latitude = :lat AND longitude= :lon")
    suspend fun selectMarker(lat: String, lon: String): MarkerDBModel

    @Query("SELECT * FROM MARKER")
    suspend fun selectAllMarker(): List<MarkerDBModel>

    @Insert()
    suspend fun insertlistMarker(markers: List<MarkerDBModel>)

    @Insert()
    suspend fun insertMarker(markerDBModel: MarkerDBModel)

    @Update
    suspend fun updateMarker(markerDBModel: MarkerDBModel)

    @Delete
    suspend fun delete(markerDBModel: MarkerDBModel)
    @Delete
    suspend fun delete(markerDBModel: List<MarkerDBModel>)
    @Query("DELETE FROM MARKER WHERE id = :id")
    suspend fun delete(id:Long)
    @Query("DELETE FROM MARKER WHERE marker.latitude = :lat AND marker.longitude = :lon")
    suspend fun delete(lat:String, lon:String)


}