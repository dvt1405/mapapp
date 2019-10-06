package com.example.showimage.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.showimage.database.model.Image
import com.example.showimage.database.model.ImageDBModel

@Dao
interface ImageDAO {
    @Query("SELECT * FROM IMAGE WHERE id = :id")
    suspend fun getImage(id: Int): ImageDBModel

    @Query("SELECT * FROM IMAGE")
    suspend fun getImage(): List<ImageDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserImage(image: ImageDBModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserImage(imageList: List<ImageDBModel>)

    @Update
    suspend fun updateImage(image: ImageDBModel)

    @Delete
    suspend fun deleteImage(image: ImageDBModel)

    @Delete
    suspend fun deleteImage(images: List<ImageDBModel>)
}