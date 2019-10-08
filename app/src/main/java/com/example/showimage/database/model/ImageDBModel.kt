package com.example.showimage.database.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class ImageDBModel {
    @PrimaryKey
    @NonNull
    var id:Long=0
    @ColumnInfo(name = "title")
    var title:String?=""
    @ColumnInfo(name = "content")
    var content:ByteArray?=null
    @ColumnInfo(name = "url")
    var url:String?=""
}