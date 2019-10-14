package com.example.showimage.database.model

import com.google.gson.annotations.SerializedName

class ListImageResponse {
    @SerializedName("photos")
    var photos: Data?=null

    class Data {
        @SerializedName("page")
        var page:String?=""
        @SerializedName("pages")
        var pages:String?=""
        @SerializedName("perpage")
        var perpage:String?=""
        @SerializedName("total")
        var total:String?=""
        @SerializedName("photo")
        var photo:List<ImageModel>?=null
    }

}