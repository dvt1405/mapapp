package com.example.showimage.repository

import com.example.showimage.database.dao.ImageMarkerDAO
import com.example.showimage.database.model.ImageModel
import com.example.showimage.database.model.ImageMarkerModel
import com.example.showimage.database.model.MarkerDBModel

class ImageMarkerRepository(private val imageMarkerDao: ImageMarkerDAO) {
    suspend fun getListImageMarkerByIDMarker(marker:MarkerDBModel) = imageMarkerDao.getImageOfMarker(marker.id!!)
    suspend fun insert(imageMarkerDBModel: ImageMarkerModel) = imageMarkerDao.insertImageOfMarker(imageMarkerDBModel)
    suspend fun getImageMarker(imageModel:ImageModel) = imageMarkerDao.getImageMarkerModel(imageModel.id!!)
    suspend fun update(imageMarkerDBModel: ImageMarkerModel) = imageMarkerDao.update(imageMarkerDBModel)
    suspend fun delete(imageModel: ImageModel) = imageMarkerDao.deleteImageOfMarker(imageModel.id!!)
}