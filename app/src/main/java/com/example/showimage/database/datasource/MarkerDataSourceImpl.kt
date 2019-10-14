package com.example.showimage.database.datasource

import android.content.Context
import com.example.showimage.database.RoomDB
import com.example.showimage.database.model.MarkerDBModel
import com.example.showimage.domain.data.datasource.MarkerDataSource
import com.example.showimage.domain.domain.MarkerCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MarkerDataSourceImpl(context: Context) : MarkerDataSource {


    private val coroutineScope = Dispatchers.IO
    private val markerDAO =
        RoomDB.getDatabase(context, coroutineScope as CoroutineScope).MarkerDAO()

    override suspend fun insert(markerCore: MarkerCore) =
        markerDAO.insertMarker(MarkerDBModel.fromMarkerCore(markerCore))

    override suspend fun delete(markerCore: MarkerCore) =
        markerDAO.delete(markerCore.latitude, markerCore.lontitide)

    override suspend fun delete(latitude: Double, lontitude: Double) = markerDAO.delete(latitude.toString(), lontitude.toString())

    override suspend fun update(markerCore: MarkerCore) =
        markerDAO.updateMarker(markerDBModel = MarkerDBModel.fromMarkerCore(markerCore))

    override suspend fun select(): List<MarkerCore> = markerDAO.selectAllMarker().map { it ->
        MarkerCore(
            it.id,
            it.lat!!,
            it.lon!!,
            it.isOldMarker,
            it.title,
            it.city
        )
    }

    override suspend fun select(lat: Double, lon: Double): MarkerCore {
        return MarkerDBModel.toMarkerCore(markerDAO.selectMarker(lat.toString(), lon.toString()))
    }
}