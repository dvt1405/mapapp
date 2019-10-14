package com.example.showimage.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.showimage.R
import com.example.showimage.Utils
import com.example.showimage.adapter.CollectionAdapter
import com.example.showimage.database.model.ImageModel
import com.example.showimage.viewmodel.ImageViewModel
import kotlinx.android.synthetic.main.activity_list_image.*
import kotlin.collections.ArrayList


class ListImageActivity : AppCompatActivity() {
    lateinit var adapter: CollectionAdapter
    lateinit var imageViewModel: ImageViewModel
    var listImageModel: List<ImageModel>? = arrayListOf()
    var page = 1
    var perPage = 10
    var latitude: Double? = 0.0
    var longtitude: Double? = 0.0
    var snipet: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_image)
        supportActionBar?.hide()
        val intent = intent
        val bundle: Bundle? = intent.extras
        latitude = bundle?.getDouble(Utils.LATITUDE_VALUES)
        longtitude = bundle?.getDouble(Utils.LONGTITUDE_VALUES)
        snipet = bundle?.getString(Utils.SNIPET)

        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel::class.java)
        adapter = CollectionAdapter(this, listImageModel as ArrayList<ImageModel>, page)
        loadImageLocalToGridView()
        gridView.adapter = adapter
        gridView.setOnItemClickListener { parent, view, position, id ->

        }

        buttonLoadImage.setOnClickListener {
            page++
            loadImageNetworkToGridview()
        }

    }

    fun loadImageNetworkToGridview() {
        imageViewModel.loadImageNetwork(latitude!!, longtitude!!, page, perPage)
            .observe(this, androidx.lifecycle.Observer {
                if (it.isNotEmpty()) {
                    adapter.pageNume = page
                    imageViewModel.insert(it, latitude!!, longtitude!!)
                    imageViewModel.downloadListImage(it)
                } else {
                    Toast.makeText(this,"No image to show",Toast.LENGTH_LONG).show()

                }
                adapter.listImage = it as ArrayList<ImageModel>
            })
    }

    fun loadImageLocalToGridView() {
        adapter.listImage =
            imageViewModel.loadImageLocal(latitude!!, longtitude!!) as ArrayList<ImageModel>
        adapter.pageNume = page
        if (adapter.listImage.isEmpty()) {
            loadImageNetworkToGridview()
        }
    }

}
