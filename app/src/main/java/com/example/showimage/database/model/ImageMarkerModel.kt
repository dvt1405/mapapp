package com.example.showimage.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_marker")
class ImageMarkerModel {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0
    @ColumnInfo(name = "idmarker")
    var id_marker: Int? = 0
    @ColumnInfo(name = "idimage")
    var id_image: Int? = 0

}