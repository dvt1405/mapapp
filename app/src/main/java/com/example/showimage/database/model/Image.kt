package com.example.showimage.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Image {
    @SerializedName("id")
    var id: String? = ""
    @SerializedName("owner")
    var owner: String? = ""
    @SerializedName("secret")
    var secret: String? = ""
    @SerializedName("farm")
    var farm: String? = ""
    @SerializedName("title")
    var title: String? = ""
    @SerializedName("ispublic")
    var isPublic:String?=""
    @SerializedName("isfriend")
    var isFriend:String?=""
    @SerializedName("isfamily")
    var isFarmily:String?=""
    @SerializedName("server")
    var server: String? =""
    var content: String? = ""
    var url: String? = ""
}