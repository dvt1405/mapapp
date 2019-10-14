package com.example.showimage.database.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "image_marker")
class ImageMarkerModel{
    @PrimaryKey
    @NonNull
    var id: String? = UUID.randomUUID().toString()
    @ColumnInfo(name = "idmarker")
    var id_marker: String? = ""
    @ColumnInfo(name = "idimage")
    var id_image: String? = ""


}