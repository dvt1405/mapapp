package com.example.showimage.repository

import androidx.lifecycle.LiveData
import com.example.showimage.database.dao.MarkerDAO
import com.example.showimage.database.model.MarkerDBModel
import io.reactivex.Flowable

class MarkerRepository(private val markerDAO: MarkerDAO) {
    suspend fun getAllMarker(): List<MarkerDBModel> = markerDAO.selectAllMarker()
    suspend fun getMarker(lat: Double, lon: Double): MarkerDBModel =
        markerDAO.selectMarker(lat.toString(), lon.toString())

    suspend fun getMarker(id: Int): MarkerDBModel = markerDAO.selectMarker(id)
    suspend fun deleteMarker(marker: MarkerDBModel) = markerDAO.delete(marker)
    suspend fun deleteMarker(markers: List<MarkerDBModel>) = markerDAO.delete(markers)
    suspend fun deleteMarker(id:Long) = markerDAO.delete(id)
    suspend fun deleteMarker(lat:Double,lon:Double) = markerDAO.delete(lat.toString(),lon.toString())
    suspend fun updateMarker(marker: MarkerDBModel) = markerDAO.updateMarker(marker)
    suspend fun insertMarker(marker: MarkerDBModel) { markerDAO.insertMarker(marker) }
    suspend fun insertMarker(markers: List<MarkerDBModel>) = markerDAO.insertlistMarker(markers)


}