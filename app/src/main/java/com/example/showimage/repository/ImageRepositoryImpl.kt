package com.example.showimage.repository

import com.example.showimage.domain.data.datasource.LocalImageDataSource
import com.example.showimage.domain.data.datasource.NetworkImageDataSource
import com.example.showimage.domain.data.repositories.ImageRepository
import com.example.showimage.domain.domain.ImageCore
import com.example.showimage.domain.domain.MarkerCore

class ImageRepositoryImpl(
    val imageNetWorkDataSource: NetworkImageDataSource,
    val imageLocalDataSource: LocalImageDataSource
) : ImageRepository {
    override suspend fun downloadImage(imageCores: List<ImageCore>)= imageNetWorkDataSource.downloadImage(imageCores)

    override suspend fun getImages(markerCore: MarkerCore): List<ImageCore> = imageLocalDataSource.select(markerCore)

    override suspend fun deleteImage(imageCore: ImageCore) = imageLocalDataSource.delete(imageCore)

    override suspend fun insertImage(imageCore: ImageCore) = imageLocalDataSource.insert(imageCore)

    override suspend fun updateImage(imageCore: ImageCore) = imageLocalDataSource.update(imageCore)
    
}