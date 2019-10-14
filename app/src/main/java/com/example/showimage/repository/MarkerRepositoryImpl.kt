package com.example.showimage.repository

import com.example.showimage.domain.data.datasource.MarkerDataSource
import com.example.showimage.domain.data.repositories.MarkerRepository
import com.example.showimage.domain.domain.MarkerCore

class MarkerRepositoryImpl(val markerDataSource: MarkerDataSource) : MarkerRepository {

    override suspend fun insert(marker: MarkerCore) = markerDataSource.insert(marker)

    override suspend fun delete(marker: MarkerCore) = markerDataSource.delete(marker)

    override suspend fun delete(latitude: Double, lontitude: Double) =
        markerDataSource.delete(latitude, lontitude)

    override suspend fun update(marker: MarkerCore) = markerDataSource.update(marker)

    override suspend fun getAll(): List<MarkerCore> = markerDataSource.select()

    override suspend fun selectByLatLon(latitude: Double, lontitude: Double): MarkerCore =
        markerDataSource.select(latitude, lontitude)
}