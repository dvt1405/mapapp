package com.example.showimage.domain.data.datasource

import com.example.showimage.domain.domain.ImageCore
import com.example.showimage.domain.domain.MarkerCore

interface LocalImageDataSource {
    suspend fun insert(imageCore: ImageCore)
    suspend fun insert(imges: List<ImageCore>)
    suspend fun delete(imageCore: ImageCore)
    suspend fun update(imageCore: ImageCore)
    suspend fun select(markerCore: MarkerCore): List<ImageCore>

}