package com.example.showimage.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.showimage.database.dao.ImageDAO
import com.example.showimage.database.dao.ImageMarkerDAO
import com.example.showimage.database.dao.MarkerDAO
import com.example.showimage.database.model.Image
import com.example.showimage.database.model.ImageMarkerModel
import com.example.showimage.database.model.ListImageResponse
import com.example.showimage.database.model.MarkerDBModel
import com.example.showimage.network.ApiCall
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Url
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.security.AccessControlContext
import javax.security.auth.callback.Callback
import retrofit2.Response
import java.lang.Exception


class ImageRepository(
    private val imageDAO: ImageDAO,
    val imageMarkerDAO: ImageMarkerDAO,
    val markerDAO: MarkerDAO
) {
    var imageFromApi: ListImageResponse? = null

    suspend fun getImages(): List<Image> = imageDAO.getImage()

    suspend fun getImage(id: String): Image = imageDAO.getImage(id)

    suspend fun insertImage(image: Image) = this.imageDAO.inserImage(image)

    suspend fun insertImage(images: List<Image>) = this.imageDAO.inserImage(images)

    suspend fun deleteImage(image: Image) = imageDAO.deleteImage(image)

    suspend fun deleteImage(images: List<Image>) = imageDAO.deleteImage(images)

    suspend fun updateImage(image: Image) = imageDAO.updateImage(image)

    fun loadImagesNetwork(
        lat: Double,
        lon: Double,
        page: Int,
        perpage: Int
    ): LiveData<List<Image>> {
        var data: MutableLiveData<List<Image>> = MutableLiveData()
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
                    data.postValue(rs.photos?.photo)
                }, { err -> err.printStackTrace() }
                )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return data
    }

    fun downloadImage(image: Image): Image {
        val url =
            "https://farm${image.farm}.staticflickr.com/${image.server}/${image.id}_${image.secret}.jpg"
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
        return image
    }

    fun downloadImage(images: List<Image>) {

        Observable.just(images)
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
                        Log.i("url", it.url.plus(" "+it.id))
                    }
                    Log.i("Download", "Done")
                }

            }, { er ->
                er.printStackTrace()
            })

    }

    fun loadImageLocal(listMarkerImage: List<ImageMarkerModel>): List<Image> {
        var images = arrayListOf<Image>()
        for (i in listMarkerImage) {
            runBlocking {
                var image: Image = getImage(i.id_image!!)
                images.add(image)
            }
        }
        return images
    }


}