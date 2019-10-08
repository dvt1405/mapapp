package com.example.showimage.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.showimage.database.model.Image

@Dao
interface ImageDAO {
    @Query("SELECT * FROM IMAGE WHERE id = :id")
    suspend fun getImage(id: String): Image

    @Query("SELECT * FROM IMAGE")
    suspend fun getImage(): List<Image>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserImage(image: Image)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserImage(imageList: List<Image>)

    @Update
    suspend fun updateImage(image: Image)

    @Delete
    suspend fun deleteImage(image: Image)

    @Delete
    suspend fun deleteImage(images: List<Image>)
}