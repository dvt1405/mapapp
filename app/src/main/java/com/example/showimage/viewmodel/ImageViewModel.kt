package com.example.showimage.viewmodel

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.showimage.R
import com.example.showimage.database.RoomDB
import com.example.showimage.database.model.ImageModel
import com.example.showimage.database.model.ImageMarkerModel
import com.example.showimage.database.model.MarkerDBModel
import com.example.showimage.repository.ImageMarkerRepository
import com.example.showimage.repository.ImageRepository
import com.example.showimage.repository.MarkerRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.NullPointerException
import java.util.*

class ImageViewModel(application: Application) :
    AndroidViewModel(application) {
    var imageRepository: ImageRepository
    var imageMarkerRepository: ImageMarkerRepository
    var markerRepository: MarkerRepository
    var listImageModel: MutableLiveData<List<ImageModel>>? = MutableLiveData()

    init {
        val roomDB: RoomDB = RoomDB.getDatabase(application, viewModelScope)
        val imageDao = roomDB.ImageDAO()
        val imageMarkerDAO = roomDB.ImageMarkerDAO()
        val markerDAO = roomDB.MarkerDAO()
        imageMarkerRepository = ImageMarkerRepository(imageMarkerDAO)
        imageRepository =
            ImageRepository(imageDao, imageMarkerDAO = imageMarkerDAO, markerDAO = markerDAO)
        markerRepository = MarkerRepository(markerDAO)
    }

    fun insert(imageModel: ImageModel) = viewModelScope.launch {
        imageRepository.insertImage(imageModel)

    }

    fun insert(imageModels: List<ImageModel>, lat: Double, lon: Double) = viewModelScope.launch {
        for (items in imageModels) {
            Log.i("ID image", items.id)
            var imageMarkerModel = imageMarkerRepository.getImageMarker(items)
            try {
                var marker: MarkerDBModel = async { markerRepository.getMarker(lat, lon) }.await()
                imageMarkerModel.id_marker = marker.id
                imageMarkerModel.id_image = items.id
                imageMarkerRepository.update(imageMarkerModel)
                imageRepository.insertImage(items)
                Log.i("update image marker ", "done")

            } catch (ex: NullPointerException) {
                imageMarkerModel = ImageMarkerModel()
                var marker: MarkerDBModel = async { markerRepository.getMarker(lat, lon) }.await()
                imageMarkerModel.id = UUID.randomUUID().toString()
                imageMarkerModel.id_marker = marker.id
                imageMarkerModel.id_image = items.id
                imageMarkerRepository.insert(imageMarkerModel)
                imageRepository.insertImage(items)
                Log.i("Insert image marker", "done")
            }

        }
    }

    fun update(imageModel: ImageModel) = viewModelScope.launch {
        imageRepository.updateImage(imageModel)
    }

    fun loadImageNetwork(
        lat: Double,
        lon: Double,
        page: Int,
        perpage: Int
    ): LiveData<List<ImageModel>> {
        var data: MutableLiveData<List<ImageModel>> = MutableLiveData()
        runBlocking {
            val liveData = async { imageRepository.loadImagesNetwork(lat, lon, page, perpage) }
            runBlocking {
                data = liveData.await() as MutableLiveData<List<ImageModel>>
            }
        }
        return data
    }

    fun loadImageLocal(lat: Double, lon: Double): List<ImageModel> {
        var data: List<ImageModel> = listOf()
        runBlocking {
            val marker: MarkerDBModel = markerRepository.getMarker(lat, lon)
            val listImageMarker = imageMarkerRepository.getListImageMarkerByIDMarker(marker)
            val images = async { imageRepository.loadImageLocal(listImageMarker) }
            runBlocking {
                data = images.await()
            }
        }
        Log.i("Size data", data.size.toString())
        return data
    }

    fun downloadImage(imageModel: ImageModel) {
        imageRepository.downloadImage(imageModel)
    }

    fun downloadListImage(imageModels: List<ImageModel>) {
            imageRepository.downloadImage(imageModels)

    }

    fun delete(imageModel: ImageModel) = viewModelScope.launch {
        imageRepository.deleteImage(imageModel)
        imageMarkerRepository.delete(imageModel)
    }


}