package com.example.showimage.repository

import com.example.showimage.database.dao.ImageMarkerDAO
import com.example.showimage.database.model.ImageMarkerModel
import com.example.showimage.database.model.MarkerDBModel

class ImageMarkerRepository(private val imageMarkerDao: ImageMarkerDAO) {
    suspend fun getListImageMarkerByIDMarker(marker:MarkerDBModel) = imageMarkerDao.getImageOfMarker(marker.id.toString())
    suspend fun insert(imageMarkerDBModel: ImageMarkerModel) = imageMarkerDao.insertImageOfMarker(imageMarkerDBModel)
}