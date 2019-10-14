package com.example.showimage.database.datasource

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.showimage.database.RoomDB
import com.example.showimage.database.model.ImageModel
import com.example.showimage.domain.data.datasource.NetworkImageDataSource
import com.example.showimage.domain.domain.ImageCore
import com.example.showimage.domain.domain.MarkerCore
import com.example.showimage.network.ApiCall
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL

class NetworkImageDataSourceImpl(context: Context) :
    NetworkImageDataSource {
    val imageDAO = RoomDB.getDatabase(context, Dispatchers.IO as CoroutineScope).ImageDAO()

    override suspend fun insert(imageCores: List<ImageCore>) = imageCores.forEach {
        imageDAO.inserImage(ImageModel.fromImageCore(it))
    }

    override suspend fun delete(imageCore: ImageCore) = imageDAO.deleteImage(ImageModel.fromImageCore(imageCore))

    override suspend fun update(imageCore: ImageCore) = imageDAO.updateImage(ImageModel.fromImageCore(imageCore))

    override suspend fun fecthImage(markerCore: MarkerCore, page: String, perPage: String): LiveData<List<ImageModel>> {
        var data: MutableLiveData<List<ImageModel>> =
            MutableLiveData()
        try {
            ApiCall().requestSearchImageForLocation(
                markerCore.latitude,
                markerCore.lontitide,
                page,
                perPage
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rs ->
                    rs.photos?.photo?.forEach {
                        val url =
                            "https://farm${it.farm}.staticflickr.com/${it.server}/${it.id}_${it.secret}.jpg"
                        it.url = url
                    }
                    data.postValue(rs.photos?.photo)
                }, { err -> err.printStackTrace() }
                )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return data
    }

    override suspend fun downloadImage(imageCores: List<ImageCore>) {
        Observable.just(imageCores)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { rs ->
                    rs.forEach {
                        val url =
                            "https://farm${it.farm}.staticflickr.com/${it.server}/${it.id}_${it.secret}.jpg"
                        val inputStream = URL(url).openStream()
                        var outputStream = ByteArrayOutputStream()
                        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
                        val byteArrayImage = outputStream.toByteArray()
                        it.bitmap = byteArrayImage
                        runBlocking {
                            update(it)
                        }
                    }
                },
                { err ->
                    err.printStackTrace()
                }
            )
    }
}