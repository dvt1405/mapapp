package com.example.showimage.database.datasource

import android.content.Context
import com.example.showimage.database.RoomDB
import com.example.showimage.database.model.ImageModel
import com.example.showimage.domain.data.datasource.LocalImageDataSource
import com.example.showimage.domain.domain.ImageCore
import com.example.showimage.domain.domain.MarkerCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalImageDataSourceImpl(context: Context) :
    LocalImageDataSource {
    private val coroutineScope = Dispatchers.IO
    private val imageDAO = RoomDB.getDatabase(context, coroutineScope as CoroutineScope).ImageDAO()
    override suspend fun insert(imageCore: ImageCore) =
        imageDAO.inserImage(ImageModel.fromImageCore(imageCore))

    override suspend fun insert(imges: List<ImageCore>) =
        imageDAO.inserImage(imges.map { i -> ImageModel.fromImageCore(i) })

    override suspend fun delete(imageCore: ImageCore) =
        imageDAO.deleteImage(ImageModel.fromImageCore(imageCore))

    override suspend fun update(imageCore: ImageCore) =
        imageDAO.updateImage(ImageModel.fromImageCore(imageCore))

    override suspend fun select(markerCore: MarkerCore): List<ImageCore> =
        imageDAO.getImages(markerCore.id!!).map { it ->
            ImageCore(
                it.id!!,
                it.owner!!,
                it.secret!!,
                it.farm!!,
                it.title!!,
                it.public!!,
                it.friend!!,
                it.farmily!!,
                it.server!!,
                it.bitmap!!,
                it.url!!
            )
        }
}