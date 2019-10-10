package com.example.showimage.database.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "marker")
class MarkerDBModel() : Parcelable {
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

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        lat = parcel.readString()
        lon = parcel.readString()
        isOldMarker = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        title = parcel.readString()
        city = parcel.readString()
        country = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(lat)
        parcel.writeString(lon)
        parcel.writeValue(isOldMarker)
        parcel.writeString(title)
        parcel.writeString(city)
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MarkerDBModel> {
        override fun createFromParcel(parcel: Parcel): MarkerDBModel {
            return MarkerDBModel(parcel)
        }

        override fun newArray(size: Int): Array<MarkerDBModel?> {
            return arrayOfNulls(size)
        }
    }
}