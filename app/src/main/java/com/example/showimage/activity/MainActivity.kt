package com.example.showimage.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.example.showimage.R
import com.example.showimage.Utils
import com.example.showimage.adapter.CustomInfoWindowAdapter
import com.example.showimage.database.model.MarkerDBModel
import com.example.showimage.viewmodel.MarkerViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.RuntimeException
import java.net.InetAddress
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener,
    GoogleMap.OnInfoWindowLongClickListener, GoogleMap.OnInfoWindowClickListener {

    private lateinit var markerViewModel: MarkerViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private lateinit var listMarker: MutableLiveData<ArrayList<MarkerDBModel>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        listMarker = MutableLiveData()
        markerViewModel = ViewModelProviders.of(this).get(MarkerViewModel::class.java)
        mapFragment.getMapAsync(this)

        zoomIn.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        }
        zommOut.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkPermission()
        geocoder = Geocoder(this, Locale.getDefault())
        mMap.setInfoWindowAdapter(
            CustomInfoWindowAdapter(layoutInflater.inflate(R.layout.custom_info_window, null))
        )
        setListMarker(this)
        showListMarker(this)
//        showAllMarkerSaved(this)
        mMap.setOnMyLocationClickListener(this)
        mMap.setOnMapClickListener(this)
        mMap.setOnInfoWindowLongClickListener(this)
        mMap.setOnInfoWindowClickListener(this)
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMapClick(p0: LatLng) {
        addMarker(p0)
        try {
            var address: List<Address>
            var markerDBModel = MarkerDBModel()
            var listMarkerTemp: ArrayList<MarkerDBModel> =
                listMarker.value as ArrayList<MarkerDBModel>

            address = getAddressMarker(p0.latitude, p0.longitude)
            markerDBModel.lat = p0.latitude.toString()
            markerDBModel.lon = p0.longitude.toString()

            listMarkerTemp.add(markerDBModel)
            listMarker.value = listMarkerTemp
            this.markerViewModel.insertMarkerOption(MarkerOptions().position(p0), address)

            Log.i("Add a marker", "done")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onInfoWindowLongClick(p0: Marker) {
        showDialogMarker(p0)
    }

    override fun onInfoWindowClick(p0: Marker) {

        var intent = Intent(this, ListImageActivity::class.java)
        intent.putExtra(Utils.LATITUDE_VALUES, p0.position.latitude)
        intent.putExtra(Utils.LONGTITUDE_VALUES, p0.position.longitude)
        intent.putExtra(Utils.SNIPET, p0.snippet)
        startActivity(intent)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (permissions.size == 1
                    && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mMap.isMyLocationEnabled = true
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onMyLocationClick(p0: Location) {
        mMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    p0.latitude,
                    p0.longitude
                )
            ).title("My location")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(p0.latitude, p0.longitude)))
        try {
            var address: List<Address> = getAddressMarker(p0.latitude, p0.longitude)
            this.markerViewModel.insertMarkerOption(
                MarkerOptions().position(
                    LatLng(
                        p0.latitude,
                        p0.longitude
                    )
                ), address
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.i("Window shown?", p0.isInfoWindowShown.toString())
        try {
            var markerDBModel =
                markerViewModel.getMarker(p0.position.latitude, p0.position.longitude)
            var address: String? = markerDBModel.city
            if (!address.isNullOrEmpty()) {
                p0.title = address
            } else {
                p0.title = getMarkerInfo(p0)[0].getAddressLine(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (p0.isInfoWindowShown) {
            p0.hideInfoWindow()
        } else {
            p0.showInfoWindow()
            mMap.moveCamera(CameraUpdateFactory.newLatLng(p0.position))
        }

        return false
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    fun setListMarker(lifecycle: LifecycleOwner) {
        markerViewModel.getAllMarker().observe(lifecycle, androidx.lifecycle.Observer {
            listMarker.postValue(it as ArrayList<MarkerDBModel>)
        })
    }

    fun showListMarker(lifecycle: LifecycleOwner) {
        mMap.clear()

        listMarker.observe(lifecycle, androidx.lifecycle.Observer {
            it.forEach {
                runOnUiThread({
                    addMarker(LatLng(it.lat!!.toDouble(), it.lon!!.toDouble()))
                })
            }
        })
    }

    fun addMarker(latLng: LatLng) {
        val marker = MarkerOptions().position(latLng)
        mMap.addMarker(marker)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    fun getAddressMarker(lat: Double, lon: Double): List<Address> {
        return geocoder.getFromLocation(lat, lon, 1)
    }

    fun getMarkerInfo(marker: Marker): List<Address> {
        return geocoder.getFromLocation(marker.position.latitude, marker.position.longitude, 1)
    }

    fun showDialogMarker(marker: Marker) {
        var dialog: AlertDialog.Builder = AlertDialog.Builder(
            this,
            R.style.MyDialogTheme
        )

        var dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_marker, null)
        var btnOk = dialogView.findViewById<Button>(R.id.btnOK)
        var btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        dialog.setView(dialogView)
        var alert = dialog.show()
        btnOk.setOnClickListener {
            var listMarkerTemp =
                listMarker.value as ArrayList<MarkerDBModel>
            with(listMarkerTemp.iterator()) {
                forEach {
                    if(it.lat!!.toDouble() == marker.position.latitude &&
                        it.lon!!.toDouble() == marker.position.longitude) {
                        remove()
                    }
                }
            }

            listMarker.value = listMarkerTemp
            markerViewModel.delete(marker.position.latitude, marker.position.longitude)

            showListMarker(this)

            alert.dismiss()

        }
        btnCancel.setOnClickListener {
            alert.dismiss()
        }
    }


}
