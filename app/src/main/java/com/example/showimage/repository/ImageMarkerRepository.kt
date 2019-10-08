package com.example.showimage.repository

import com.example.showimage.database.dao.ImageMarkerDAO
import com.example.showimage.database.model.Image
import com.example.showimage.database.model.ImageMarkerModel
import com.example.showimage.database.model.MarkerDBModel

class ImageMarkerRepository(private val imageMarkerDao: ImageMarkerDAO) {
    suspend fun getListImageMarkerByIDMarker(marker:MarkerDBModel) = imageMarkerDao.getImageOfMarker(marker.id!!)
    suspend fun insert(imageMarkerDBModel: ImageMarkerModel) = imageMarkerDao.insertImageOfMarker(imageMarkerDBModel)
    suspend fun getImageMarker(image:Image) = imageMarkerDao.getImageMarkerModel(image.id!!)
    suspend fun update(imageMarkerDBModel: ImageMarkerModel) = imageMarkerDao.update(imageMarkerDBModel)
    suspend fun delete(image: Image) = imageMarkerDao.deleteImageOfMarker(image.id!!)
}