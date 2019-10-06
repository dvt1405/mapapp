package com.example.showimage.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.showimage.database.model.ImageMarkerModel

@Dao
interface ImageMarkerDAO {
    @Query("SELECT * FROM image_marker WHERE idmarker = :id")
    suspend fun getImageOfMarker(id: Int): ImageMarkerModel

    @Update
    suspend fun updateImageMarker(imageMarkerModel: ImageMarkerModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageOfMarker(imageMarkerModel: ImageMarkerModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageOfMarker(imageMarkerModels: List<ImageMarkerModel>)

    @Delete
    suspend fun deleteImageOfMarker(imageMarkerModel: ImageMarkerModel)

    @Delete
    suspend  fun deleteImageOfMarker(imageMarkerModel: List<ImageMarkerModel>)
}