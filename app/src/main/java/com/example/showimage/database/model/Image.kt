package com.example.showimage.database.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "image")
class Image {
    @SerializedName("id")
    @PrimaryKey
    @NonNull
    var id: String? = ""
    @SerializedName("owner")
    @ColumnInfo(name = "owner")
    var owner: String? = ""
    @SerializedName("secret")
    @ColumnInfo(name = "secret")
    var secret: String? = ""
    @SerializedName("farm")
    @ColumnInfo(name = "farm")
    var farm: String? = ""
    @SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String? = ""

    @SerializedName("ispublic")
    @ColumnInfo(name = "public")
    var public: String? = ""

    @SerializedName("isfriend")
    @ColumnInfo(name = "friend")
    var friend: String? = ""
    @SerializedName("isfamily")
    @ColumnInfo(name = "family")
    var farmily: String? = ""
    @SerializedName("server")
    @ColumnInfo(name = "server")
    var server: String? = ""
    @ColumnInfo(name = "bitmap")
    var bitmap: ByteArray? = null
    @ColumnInfo(name = "url")
    var url: String? = ""



}