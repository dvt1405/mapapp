package com.example.showimage.domain.data.datasource

import com.example.showimage.domain.domain.MarkerCore

interface MarkerDataSource {
    suspend fun insert(markerCore:MarkerCore)
    suspend fun delete(markerCore: MarkerCore)
    suspend fun delete(latitude: Double, lontitude: Double)
    suspend fun update(markerCore: MarkerCore)
    suspend fun select(): List<MarkerCore>
    suspend fun select(lat: Double, lon: Double): MarkerCore
}