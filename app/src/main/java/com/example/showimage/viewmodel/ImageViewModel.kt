package com.example.showimage.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.showimage.database.RoomDB
import com.example.showimage.database.model.Image
import com.example.showimage.database.model.ImageDBModel
import com.example.showimage.database.model.ListImageResponse
import com.example.showimage.database.model.MarkerDBModel
import com.example.showimage.repository.ImageRepository
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ImageViewModel(application: Application) : AndroidViewModel(application) {
    var imageRepository: ImageRepository
    lateinit var listImage: MutableLiveData<List<ImageDBModel>>
    init {
        val imageDao = RoomDB.getDatabase(application, viewModelScope).imageDAO()
        imageRepository = ImageRepository(imageDao)
    }

    fun insert(image: ImageDBModel) = viewModelScope.launch {
        imageRepository.insertImage(image)
    }

    fun downLoadImage(): ByteArray {

        return byteArrayOf()
    }

    fun saveImageFlickr(image: Image) {

    }

    fun loadImage(
        lat: Double,
        lon: Double,
        page: Int,
        perpage: Int
    ):LiveData<List<Image>> {
        var data:MutableLiveData<List<Image>> =MutableLiveData()

        runBlocking {
            val liveData = async { imageRepository.getImagesNetwork(lat,lon,page,perpage) }
            runBlocking {
                data = liveData.await() as MutableLiveData<List<Image>>
            }
        }
        return data

    }
}