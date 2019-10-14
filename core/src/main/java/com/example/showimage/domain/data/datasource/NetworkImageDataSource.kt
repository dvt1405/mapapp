package com.example.showimage.domain.data.datasource

import com.example.showimage.domain.domain.ImageCore
import com.example.showimage.domain.domain.MarkerCore

interface NetworkImageDataSource {
    suspend fun insert(imges: List<ImageCore>)
    suspend fun delete(imageCore: ImageCore)
    suspend fun update(imageCore: ImageCore)
    suspend fun fecthImage(markerCore: MarkerCore, page: String, perPage: String): Any?
    suspend fun downloadImage(imageCores: List<ImageCore>)
}