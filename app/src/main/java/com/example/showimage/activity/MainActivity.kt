package com.example.showimage.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.example.showimage.R
import com.example.showimage.Utils
import com.example.showimage.adapter.CustomInfoWindowAdapter
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
import java.net.InetAddress
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener,
    GoogleMap.OnInfoWindowLongClickListener, GoogleMap.OnInfoWindowClickListener {

    private lateinit var markerViewModel: MarkerViewModel
    private lateinit var listLocation: List<LatLng>
    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
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
        showAllMarkerSaved(this)
        mMap.setOnMyLocationClickListener(this)
        mMap.setOnMapClickListener(this)
        mMap.setOnInfoWindowLongClickListener(this)
        mMap.setOnInfoWindowClickListener(this)
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMapClick(p0: LatLng) {
        addMarker(p0)
        this.markerViewModel.insertMarkerOption(MarkerOptions().position(p0))
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
        this.markerViewModel.insertMarkerOption(
            MarkerOptions().position(
                LatLng(
                    p0.latitude,
                    p0.longitude
                )
            )
        )
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.i("Window shown?", p0.isInfoWindowShown.toString())
        var address: List<Address> =
            geocoder.getFromLocation(p0.position.latitude, p0.position.longitude, 1)
        try {
            p0.title = address.get(0).getAddressLine(0)
        } catch (ex: IndexOutOfBoundsException) {
            p0.title = "No info for this location"
        }
        if (p0.isInfoWindowShown) {
            p0.hideInfoWindow()
        } else {
            p0.showInfoWindow()
            mMap.moveCamera(CameraUpdateFactory.newLatLng(p0.position))
        }

        return true
    }

    fun getMarkerInfo(marker: Marker): List<Address> {
        return geocoder.getFromLocation(marker.position.latitude, marker.position.longitude, 1)
    }

    fun isNetworkAcailable(context: Context): Boolean {
        var connectManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectManager.activeNetworkInfo != null && connectManager.activeNetworkInfo.isConnected
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

    fun showAllMarkerSaved(lifecycle: LifecycleOwner) {
        mMap.clear()
        markerViewModel.getAllMarker().observe(lifecycle, androidx.lifecycle.Observer {
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
            markerViewModel.delete(marker.position.latitude, marker.position.longitude)
            marker.remove()
            alert.dismiss()

        }
        btnCancel.setOnClickListener {
            alert.dismiss()
        }
    }

}
