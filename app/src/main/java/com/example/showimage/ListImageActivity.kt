package com.example.showimage

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.example.showimage.adapter.CollectionAdapter
import com.example.showimage.database.model.Image
import com.example.showimage.viewmodel.ImageViewModel
import kotlinx.android.synthetic.main.activity_list_image.*
import java.io.InputStream
import java.net.URL
import kotlin.collections.ArrayList


class ListImageActivity : AppCompatActivity() {
    lateinit var adapter: CollectionAdapter
    lateinit var imageViewModel: ImageViewModel
    var listImage: List<Image>? = arrayListOf()
    var lifecycleOwner: LifecycleOwner = this
    var page = 1
    var perPage = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_image)
        supportActionBar?.hide()
        val intent = intent
        val bundle: Bundle? = intent.extras
        var latitude: Double = bundle!!.getDouble(Utils.LATITUDE_VALUES)
        var longtitude: Double = bundle!!.getDouble(Utils.LONGTITUDE_VALUES)
        var snipet: String? = bundle!!.getString(Utils.SNIPET)
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel::class.java)
        adapter = CollectionAdapter(this, listImage)
        gridView.adapter = adapter

        imageViewModel.loadImage(latitude, longtitude, page, perPage)
            .observe(this, androidx.lifecycle.Observer {
                adapter.listImage = it as ArrayList<Image>
            })

    }

    suspend fun loadImage(result: Image) {
        var url = "https://farm${result.farm}.staticflickr.com/${result.server}/${result.id}_${result.secret}.jpg"
        var inputStreamThumbnail: InputStream? = URL(url).openStream()
        var bitmap = BitmapFactory.decodeStream(inputStreamThumbnail)
    }

}
