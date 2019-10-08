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
    var page = 0
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
        adapter = CollectionAdapter(this, listImage as ArrayList<Image>, 0)
        gridView.adapter = adapter
        loadImageLocalToGridView()

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
                if (it.size != 0) {
                    it.forEach {
                        val url =
                            "https://farm${it.farm}.staticflickr.com/${it.server}/${it.id}_${it.secret}.jpg"
                        it.url = url
                    }
                    adapter.listImage = it as ArrayList<Image>
                    adapter.pageNume = page
                    imageViewModel.insert(it, latitude!!, longtitude!!)
                    imageViewModel.downloadListImage(it)
                }
            })
    }

    fun loadImageLocalToGridView() {
        adapter.listImage =
            imageViewModel.loadImageLocal(latitude!!, longtitude!!) as ArrayList<Image>
        adapter.pageNume = page
    }

}
