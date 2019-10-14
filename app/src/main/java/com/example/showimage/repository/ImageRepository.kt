package com.example.showimage.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.showimage.database.dao.ImageDAO
import com.example.showimage.database.dao.ImageMarkerDAO
import com.example.showimage.database.dao.MarkerDAO
import com.example.showimage.database.model.ImageModel
import com.example.showimage.database.model.ImageMarkerModel
import com.example.showimage.database.model.ListImageResponse
import com.example.showimage.domain.data.datasource.LocalImageDataSource
import com.example.showimage.domain.data.datasource.NetworkImageDataSource
import com.example.showimage.domain.domain.MarkerCore
import com.example.showimage.network.ApiCall
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import retrofit2.Response


class ImageRepository(
    private val imageDAO: ImageDAO,
    val imageMarkerDAO: ImageMarkerDAO,
    val markerDAO: MarkerDAO
) {
    var imageFromApi: ListImageResponse? = null

    suspend fun getImages(markerCore: MarkerCore): List<ImageModel> = imageDAO.getImage()

    suspend fun getImage(id: String): ImageModel = imageDAO.getImage(id)

    suspend fun insertImage(imageModel: ImageModel) = this.imageDAO.inserImage(imageModel)

    suspend fun insertImage(imageModels: List<ImageModel>) = this.imageDAO.inserImage(imageModels)

    suspend fun deleteImage(imageModel: ImageModel) = imageDAO.deleteImage(imageModel)

    suspend fun deleteImage(imageModels: List<ImageModel>) = imageDAO.deleteImage(imageModels)

    suspend fun updateImage(imageModel: ImageModel) = imageDAO.updateImage(imageModel)

    fun loadImagesNetwork(
        lat: Double,
        lon: Double,
        page: Int,
        perpage: Int
    ): LiveData<List<ImageModel>> {
        var data: MutableLiveData<List<ImageModel>> = MutableLiveData()
        try {
            ApiCall().requestSearchImageForLocation(
                lat.toString(),
                lon.toString(),
                page.toString(),
                perpage.toString()
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

    fun downloadImage(imageModel: ImageModel): ImageModel {
        val url =
            "https://farm${imageModel.farm}.staticflickr.com/${imageModel.server}/${imageModel.id}_${imageModel.secret}.jpg"
        ApiCall().downloadImage(url).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response == null || !response.isSuccessful || response.body() == null || response.errorBody() != null) {
                    return
                }
                val bytes = response.body()!!.bytes()
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
        return imageModel
    }

     fun downloadImage(imageModels: List<ImageModel>) {
        Observable.just(imageModels)
            .subscribeOn(Schedulers.io())
            .subscribe({ rs ->
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
                        updateImage(it)
                    }
                }

            }, { er ->
                er.printStackTrace()
            })

    }

    fun loadImageLocal(listMarkerImage: List<ImageMarkerModel>): List<ImageModel> {
        var images = arrayListOf<ImageModel>()
        for (i in listMarkerImage) {
            runBlocking {
                var imageModel: ImageModel = getImage(i.id_image!!)
                images.add(imageModel)
            }
        }
        return images
    }

}