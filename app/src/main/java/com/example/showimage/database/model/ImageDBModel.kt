package com.example.showimage.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image")
class ImageDBModel {
    @PrimaryKey
    var id:Long=0
    @ColumnInfo(name = "title")
    var title:String?=""
    @ColumnInfo(name = "content")
    var content:ByteArray?=null
    @ColumnInfo(name = "url")
    var url:String?=""
}