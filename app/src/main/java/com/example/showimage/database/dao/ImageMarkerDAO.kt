package com.example.showimage.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.showimage.database.model.Image
import com.example.showimage.database.model.ImageMarkerModel

@Dao
interface ImageMarkerDAO {
    @Query("SELECT * FROM image_marker WHERE idmarker = :id")
    suspend fun getImageOfMarker(id: String): List<ImageMarkerModel>

    @Query("SELECT * FROM image_marker WHERE idimage = :idImage")
    suspend fun getImageMarkerModel(idImage: String): ImageMarkerModel

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

    @Update
    suspend fun update(imageMarkerModel: ImageMarkerModel)

    @Query("DELETE FROM IMAGE_MARKER WHERE idimage = :idImage")
    suspend fun deleteImageOfMarker(idImage: String)
}