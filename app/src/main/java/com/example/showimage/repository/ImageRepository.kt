package com.example.showimage.repository

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.showimage.database.dao.ImageDAO
import com.example.showimage.database.model.Image
import com.example.showimage.database.model.ImageDBModel
import com.example.showimage.database.model.ImageMarkerModel
import com.example.showimage.database.model.ListImageResponse
import com.example.showimage.network.ApiCall
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.security.AccessControlContext
import javax.security.auth.callback.Callback

class ImageRepository(private val imageDAO: ImageDAO) {
    var imageFromApi: ListImageResponse? = null

    suspend fun getImagesLocal(): List<ImageDBModel> = imageDAO.getImage()

    suspend fun insertImage(image: ImageDBModel) = this.imageDAO.inserImage(image)

    suspend fun insertImage(images: List<ImageDBModel>) = this.imageDAO.inserImage(images)

    suspend fun deleteImage(image: ImageDBModel) = imageDAO.deleteImage(image)

    suspend fun deleteImage(images: List<ImageDBModel>) = imageDAO.deleteImage(images)

    suspend fun updateImage(image: ImageDBModel) = imageDAO.updateImage(image)

    suspend fun getImagesNetwork(lat: Double, lon: Double, page: Int,perpage: Int): LiveData<List<Image>> {
        var data: MutableLiveData<List<Image>> = MutableLiveData()
        ApiCall().requestSearchImageForLocation(lat.toString(), lon.toString(), page.toString(),perpage.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rs ->
                data.postValue(rs.photos?.photo)
            }, { err -> err.printStackTrace() }
            )
        return data
    }


}