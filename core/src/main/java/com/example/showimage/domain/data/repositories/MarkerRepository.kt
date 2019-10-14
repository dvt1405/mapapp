package com.example.showimage.domain.data.repositories

import com.example.showimage.domain.domain.MarkerCore

interface MarkerRepository {
    suspend fun insert(marker: MarkerCore)
    suspend fun delete(marker: MarkerCore)
    suspend fun update(marker: MarkerCore)
    suspend fun getAll(): List<MarkerCore>
    suspend fun selectByLatLon(latitude: Double, lontitude: Double): MarkerCore
    suspend fun delete(latitude: Double, lontitude: Double)
}