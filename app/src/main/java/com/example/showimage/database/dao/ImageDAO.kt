package com.example.showimage.database.dao

import androidx.room.*
import com.example.showimage.database.model.ImageModel

@Dao
interface ImageDAO {
    @Query("SELECT * FROM IMAGE WHERE id = :id")
    suspend fun getImage(id: String): ImageModel

    @Query("SELECT * FROM IMAGE")
    suspend fun getImage(): List<ImageModel>

    @Query("SELECT image.* FROM image, image_marker WHERE image_marker.idmarker = :idMarker AND image.id = image_marker.idimage ")
    suspend fun getImages(idMarker: String): List<ImageModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserImage(imageModel: ImageModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserImage(imageModelList: List<ImageModel>)

    @Update
    suspend fun updateImage(imageModel: ImageModel)

    @Delete
    suspend fun deleteImage(imageModel: ImageModel)

    @Delete
    suspend fun deleteImage(imageModels: List<ImageModel>)
}