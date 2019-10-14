package com.example.showimage.domain.data.repositories

import com.example.showimage.domain.domain.ImageCore
import com.example.showimage.domain.domain.MarkerCore

interface ImageRepository {
    suspend fun getImages(markerCore: MarkerCore): List<ImageCore>
    suspend fun deleteImage(imageCore: ImageCore)
    suspend fun insertImage(imageCore: ImageCore)
    suspend fun updateImage(imageCore: ImageCore)
    suspend fun downloadImage(imageCores: List<ImageCore>)
}