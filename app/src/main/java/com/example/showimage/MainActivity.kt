package com.example.showimage

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.room.RoomDatabase
import com.example.showimage.adapter.CustomInfoWindowAdapter
import com.example.showimage.database.RoomDB
import com.example.showimage.database.model.MarkerDBModel
import com.example.showimage.network.ApiCall
import com.example.showimage.view.InfoWindowDialogFragment
import com.example.showimage.viewmodel.MarkerViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.observers.SubscriberCompletableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog_marker.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.GET
import java.lang.Exception
import java.net.InetAddress
import java.util.*
import java.util.zip.Inflater

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


        zoomIn.setOnClickListener(View.OnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        })
        zommOut.setOnClickListener(View.OnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkPermission()
        geocoder = Geocoder(this, Locale.getDefault())
        mMap.setInfoWindowAdapter(
            CustomInfoWindowAdapter(layoutInflater.inflate(R.layout.custom_info_window,null)))
        showAllMarkerSaved(this)
        mMap.setOnMyLocationClickListener(this)
        mMap.setOnMapClickListener(this)
        mMap.setOnInfoWindowLongClickListener(this)
        mMap.setOnInfoWindowClickListener(this)
        mMap.setOnMarkerClickListener(this)
        mMap.setOnInfoWindowLongClickListener(GoogleMap.OnInfoWindowLongClickListener {
            showDialogMarker(it)
        })
    }

    override fun onMapClick(p0: LatLng) {
        addMarker(p0)
        this.markerViewModel.insertMarkerOption(MarkerOptions().position(p0))
    }

    override fun onInfoWindowLongClick(p0: Marker) {
        showDialogMarker(p0)
    }
    override fun onInfoWindowClick(p0: Marker) {

        var intent: Intent = Intent(this, ListImageActivity::class.java)
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(p0.latitude,p0.longitude)))
        this.markerViewModel.insertMarkerOption(MarkerOptions().position(LatLng(p0.latitude,p0.longitude)))
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.i("Window shown?",p0.isInfoWindowShown.toString())
        var address: List<Address> =
            geocoder.getFromLocation(p0.position.latitude, p0.position.longitude, 1)
        try {
            p0.title = address.get(0).getAddressLine(0)
        } catch (ex: IndexOutOfBoundsException) {
            p0.title = "No info for this location"
        }
        if(p0.isInfoWindowShown){
            p0.hideInfoWindow()
        }else{
            p0.showInfoWindow()
            mMap.moveCamera(CameraUpdateFactory.newLatLng(p0.position))
        }

        return true
    }

    fun getMarkerInfo(marker: Marker): List<Address> {
        return geocoder.getFromLocation(marker.position.latitude, marker.position.longitude, 1)
    }

    fun checkInternetConnection(): Boolean {
        try {
            var inet: InetAddress = InetAddress.getByName("google.com")
            return !inet.equals("")
        } catch (e: Exception) {
            return false
        }
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

    fun focusLastMarker() {

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

    fun showDialogMarker(marker:Marker) {
        var dialog:AlertDialog.Builder = AlertDialog.Builder(this,R.style.MyDialogTheme)
        var dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_marker,null)
        var btnOk = dialogView.findViewById<Button>(R.id.btnOK)
        var btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        dialog.setView(dialogView)
        var alert = dialog.show()
        btnOk.setOnClickListener{
            markerViewModel.delete(marker.position.latitude,marker.position.longitude)
            marker.remove()
            alert.dismiss()

        }
        btnCancel.setOnClickListener{
            alert.dismiss()
        }
    }

}
