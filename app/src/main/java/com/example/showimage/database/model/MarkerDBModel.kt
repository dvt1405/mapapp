package com.example.showimage.database.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "marker")
class MarkerDBModel {
    @PrimaryKey
    @NonNull
    var id:String?= UUID.randomUUID().toString()
    @ColumnInfo(name = "latitude")
    var lat:String?=""
    @ColumnInfo(name = "longitude")
    var lon:String?=""
    @ColumnInfo(name = "member")
    var isOldMarker:Boolean?=false
}