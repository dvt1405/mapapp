package com.example.showimage.adapter

import android.view.View
import android.widget.TextView
import com.example.showimage.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(private val customView:View):GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(p0: Marker?): View {
        var textView:TextView = customView.findViewById(R.id.textViewInfoWindow)
        textView.setText(p0?.title)
        return customView
    }

    override fun getInfoWindow(p0: Marker?): View {
        var textView:TextView = customView.findViewById(R.id.textViewInfoWindow)
        textView.setText(p0?.title)
        return customView
    }
}