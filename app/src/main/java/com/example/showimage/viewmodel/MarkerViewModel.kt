package com.example.showimage.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showimage.database.RoomDB
import com.example.showimage.database.model.Image
import com.example.showimage.database.model.MarkerDBModel
import com.example.showimage.repository.MarkerRepository
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Flowable
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MarkerViewModel(application: Application) : AndroidViewModel(application) {
    var markerRepository: MarkerRepository
    var listMarker: List<MarkerDBModel> = arrayListOf()
    lateinit var listLiveDataMarker: MutableLiveData<List<MarkerDBModel>>
    init {
        val markerDao = RoomDB.getDatabase(application,viewModelScope).MarkerDAO()
        markerRepository = MarkerRepository(markerDao)

        runBlocking {
            val liveData = async { markerRepository.getAllMarker()}
            runBlocking {
                listMarker = liveData.await()
            }
        }

        if(!::listLiveDataMarker.isInitialized) {
            listLiveDataMarker = MutableLiveData()

            viewModelScope.launch {
                listLiveDataMarker.postValue(markerRepository.getAllMarker())
            }
        }
    }

    fun insertMarkerOption(marker: MarkerOptions) {
        var markerDBModel = MarkerDBModel()
        markerDBModel.lat = marker.position.latitude.toString()
        markerDBModel.lon = marker.position.longitude.toString()
        markerDBModel.isOldMarker = true
        viewModelScope.launch {
            markerRepository.insertMarker(markerDBModel)
        }
    }

    fun insert(listMarkerDBModel: MarkerDBModel){
        viewModelScope.launch {
            markerRepository.insertMarker(listMarkerDBModel)
        }
    }
    fun delete(marker: Marker) {
        viewModelScope.launch {
            markerRepository.deleteMarker(marker.id.toLong())
        }
    }
    fun delete(lat:Double,lon:Double) {
        viewModelScope.launch {
            markerRepository.deleteMarker(lat,lon)
        }
    }
    fun getAllMarker(): LiveData<List<MarkerDBModel>> {
        if(!::listLiveDataMarker.isInitialized) {
            listLiveDataMarker = MutableLiveData()

            viewModelScope.launch {
                listLiveDataMarker.postValue(markerRepository.getAllMarker())
            }
        }
        return listLiveDataMarker
    }
    fun getMarker(lat:Double,lon:Double):MarkerDBModel {
        var marker = MarkerDBModel()
        viewModelScope.launch {
            marker = markerRepository.getMarker(lat,lon)
        }
        return marker
    }


}