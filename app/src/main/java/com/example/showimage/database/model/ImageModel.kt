package com.example.showimage.database.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.showimage.domain.domain.ImageCore
import com.google.gson.annotations.SerializedName

@Entity(tableName = "image")
class ImageModel {
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

    companion object {
        fun toImageDomain(imageModel: ImageModel): ImageCore {
            return ImageCore(
                imageModel.id!!,
                imageModel.owner!!,
                imageModel.secret!!,
                imageModel.farm!!,
                imageModel.title!!,
                imageModel.public!!,
                imageModel.friend!!,
                imageModel.farmily!!,
                imageModel.server!!,
                imageModel.bitmap!!,
                imageModel.url!!
            )
        }

        fun toListImageDomain(images: List<ImageModel>): List<ImageCore> {
            var imageCores = arrayListOf<ImageCore>()
            images.forEach {
                imageCores.add(ImageModel.toImageDomain(it))
            }
            return imageCores
        }

        fun fromImageCore(imageCore: ImageCore): ImageModel {
            var img = ImageModel()

            img.id = imageCore.id
            img.owner = imageCore.owner
            img.secret = imageCore.secret
            img.farm = imageCore.farm
            img.title = imageCore.title
            img.public = imageCore.public
            img.friend = imageCore.friend
            img.farmily = imageCore.farmily
            img.server = imageCore.server
            img.bitmap = imageCore.bitmap
            img.url = imageCore.url

            return img
        }
    }


}