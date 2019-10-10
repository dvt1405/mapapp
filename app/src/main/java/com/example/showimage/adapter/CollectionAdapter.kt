package com.example.showimage.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.showimage.R
import com.example.showimage.database.model.Image
import java.io.ByteArrayInputStream
import java.lang.Exception


class CollectionAdapter(
    private var context: Context,
    private var listImageData: ArrayList<Image> = arrayListOf(),
    private var page: Int
) : BaseAdapter() {

    var listImage
        get() = listImageData
        set(value) {
            this.listImageData.addAll(value)
            notifyDataSetChanged()
        }
    var pageNume
        get() = page
        set(value) {
            page = value
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View
        var viewHolder: ViewHolder
        if (convertView == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.grid_item_layout, null)
            viewHolder = ViewHolder((view))
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        loadImage(listImage[position],viewHolder.textView!!,viewHolder.imageView!!)

        return view
    }

    override fun getItem(position: Int): Any = this.listImageData!!.get(position)

    override fun getItemId(position: Int): Long = this.listImageData!!.get(position).id!!.toLong()

    override fun getCount(): Int = this.listImageData!!.size

    fun loadImageBitmap(arr: ByteArray, view: ImageView) {
        val arrayInputStream = ByteArrayInputStream(arr)
        val bitmap = BitmapFactory.decodeStream(arrayInputStream)
        Glide.with(context).load(bitmap).into(view)
    }

    fun loadImageNetwork(url: String, imgItem: ImageView) {
        Glide.with(context).load(url).into(imgItem)
    }

    fun loadImage(items:Image,textView:TextView,imageView:ImageView) {
        if (this.listImageData.isNotEmpty()) {
            Log.i("size", listImageData.size.toString())
            try {
                textView.text = items.title
                if (items.bitmap != null) {
                    Log.i("bitmap", "load done")
                    loadImageBitmap(items.bitmap!!, imageView)
                } else if (items.url!!.isNotEmpty()) {
                    loadImageNetwork(items.url.toString(), imageView)
                }
            } catch (err: Exception) {
                err.printStackTrace()
            }
        } else {
            Toast.makeText(context, "No image to show", Toast.LENGTH_LONG).show()
        }
    }

    class ViewHolder(row: View?) {
        var imageView: ImageView? = null
        var textView: TextView? = null

        init {
            imageView = row?.findViewById(R.id.imageItem)
            textView = row?.findViewById(R.id.titleImage)
        }
    }

}