package com.example.showimage.domain.domain

data class ImageCore(

    var id: String,

    var owner: String,

    var secret: String,

    var farm: String,

    var title: String,

    var public: String,

    var friend: String,

    var farmily: String,

    var server: String,

    var bitmap: ByteArray,

    var url: String
) {


}