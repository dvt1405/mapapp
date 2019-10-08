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


class CollectionAdapter(
    private var context: Context,
    private var listImageData: ArrayList<Image> = arrayListOf(),
    private var page: Int
) : BaseAdapter() {

    var contextAdapter: Context
        get() = this.context
        set(value) {
            this.context = value
        }
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
        val view: View
        val viewHolder: ViewHolder
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

        if (this.listImage.size > 0) {
            val items = this.listImage.get(position)
            viewHolder.textView!!.setText(items.title)
            if (items.bitmap != null) {
                Log.i("bitmap", "load done")
                loadImageBitmap(items.bitmap!!, viewHolder.imageView!!)
            } else if (items.url!!.length > 0) {
                loadImageNetwork(items.url.toString(), viewHolder.imageView!!)
            }
        } else {
            Toast.makeText(context, "No image to show", Toast.LENGTH_LONG).show()
        }
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

    class ViewHolder(row: View?) {
        var imageView: ImageView? = null
        var textView: TextView? = null

        init {
            imageView = row?.findViewById(R.id.imageItem)
            textView = row?.findViewById(R.id.titleImage)
        }
    }

}