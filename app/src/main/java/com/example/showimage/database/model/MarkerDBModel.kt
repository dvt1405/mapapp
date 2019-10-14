package com.example.showimage.database.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.showimage.domain.domain.MarkerCore
import java.util.*

@Entity(tableName = "marker")
class MarkerDBModel() {
    @PrimaryKey
    @NonNull
    var id: String? = UUID.randomUUID().toString()
    @ColumnInfo(name = "latitude")
    var lat: String? = ""
    @ColumnInfo(name = "longitude")
    var lon: String? = ""
    @ColumnInfo(name = "member")
    var isOldMarker: Boolean? = false
    @ColumnInfo(name = "title")
    var title: String? = ""
    @ColumnInfo(name = "city")
    var city: String? = ""
    @ColumnInfo(name = "country")
    var country: String? = ""

    companion object{
        fun toMarkerCore(markerDBModel: MarkerDBModel): MarkerCore {
            return MarkerCore(markerDBModel.id!!,
                markerDBModel.lat!!,
                markerDBModel.lon!!,
                markerDBModel.isOldMarker,
                markerDBModel.title,
                markerDBModel.city)
        }
        fun fromMarkerCore(markerCore: MarkerCore): MarkerDBModel{
            var markerDBModel = MarkerDBModel()
            markerDBModel.id = markerCore.id
            markerDBModel.lat = markerCore.latitude
            markerDBModel.lon = markerCore.lontitide
            markerDBModel.isOldMarker = markerCore.old
            markerDBModel.title = markerCore.title
            markerDBModel.city = markerCore.city
            return markerDBModel
        }
    }

}