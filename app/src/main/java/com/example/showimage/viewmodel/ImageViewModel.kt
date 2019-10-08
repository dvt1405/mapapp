package com.example.showimage.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.showimage.database.RoomDB
import com.example.showimage.database.model.Image
import com.example.showimage.database.model.ImageMarkerModel
import com.example.showimage.database.model.ListImageResponse
import com.example.showimage.database.model.MarkerDBModel
import com.example.showimage.repository.ImageMarkerRepository
import com.example.showimage.repository.ImageRepository
import com.example.showimage.repository.MarkerRepository
import com.google.android.gms.maps.model.Marker
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.http.Url
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.function.LongFunction

class ImageViewModel(application: Application) : AndroidViewModel(application) {
    var imageRepository: ImageRepository
    var imageMarerRepository: ImageMarkerRepository
    var markerRepository: MarkerRepository
    var listImage: MutableLiveData<List<Image>>? = MutableLiveData()

    init {
        val roomDB: RoomDB = RoomDB.getDatabase(application, viewModelScope)
        val imageDao = roomDB.imageDAO()
        val imageMarkerDAO = roomDB.ImageMarkerDAO()
        val markerDAO = roomDB.MarkerDAO()
        imageMarerRepository = ImageMarkerRepository(imageMarkerDAO)
        imageRepository =
            ImageRepository(imageDao, imageMarkerDAO = imageMarkerDAO, markerDAO = markerDAO)
        markerRepository = MarkerRepository(markerDAO)
    }

    fun insert(image: Image) = viewModelScope.launch {
        imageRepository.insertImage(image)

    }

    fun insert(images: List<Image>, lat: Double, lon: Double) = viewModelScope.launch {
        Log.i("Size insert", images.size.toString())
        val imageMarkerModel = ImageMarkerModel()
        val marker: MarkerDBModel = markerRepository.getMarker(lat, lon)
        imageMarkerModel.id_marker = marker.id
        Log.i("ID marker", marker.id)
        for (items in images) {
            Log.i("ID image", items.id)
            imageMarkerModel.id_image = items.id
            imageMarerRepository.insert(imageMarkerModel)
            Log.i("Insert", "done")
        }
        imageRepository.insertImage(images)
    }


    fun saveImageFlickr(image: Image) {

    }

    fun update(image: Image) = viewModelScope.launch {

        imageRepository.updateImage(image)
    }

    fun loadImageNetwork(
        lat: Double,
        lon: Double,
        page: Int,
        perpage: Int
    ): LiveData<List<Image>> {
        var data: MutableLiveData<List<Image>> = MutableLiveData()

        runBlocking {
            val liveData = async { imageRepository.getImagesNetwork(lat, lon, page, perpage) }
            runBlocking {
                data = liveData.await() as MutableLiveData<List<Image>>
            }
        }
        listImage = data
        return data

    }

    fun loadImageLocal(lat: Double, lon: Double): List<Image> {
        var data: List<Image> = listOf()
        runBlocking {
            val marker: MarkerDBModel = markerRepository.getMarker(lat, lon)
            val listImageMarker = imageMarerRepository.getListImageMarkerByIDMarker(marker)
            val images = async { imageRepository.loadImageLocal(listImageMarker) }
            runBlocking {
                data = images.await()
            }
        }
        Log.i("Size data", data.size.toString())
        return data
    }

    fun downloadImage(image: Image) {
        imageRepository.downloadImage(image)

    }

    fun downloadListImage(images: List<Image>) {
        imageRepository.downloadImage(images)
    }

}