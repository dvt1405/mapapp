package com.example.showimage.view

import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.showimage.viewmodel.MarkerViewModel

class InfoWindowDialogFragment(application: Application): DialogFragment(){
    var lat:Double?=0.0
    var lon:Double?=0.0
    lateinit var viewModel:MarkerViewModel
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MarkerViewModel::class.java)
        var builder:AlertDialog.Builder = AlertDialog.Builder(activity)
        var bundle:Bundle? = arguments
        if(bundle!=null) {
            lat = bundle.getDouble("lat")
            lon = bundle.getDouble("long")
        }
        builder.setMessage("Are you sure to delete it?")
            .setPositiveButton("OK",DialogInterface.OnClickListener({dialog, which -> viewModel.delete(lat!!,lon!!) }))
            .setNegativeButton("Cancel",DialogInterface.OnClickListener({dialog, which -> }))
        return builder.create()

    }

}