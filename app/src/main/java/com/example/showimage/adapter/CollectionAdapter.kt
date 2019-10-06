package com.example.showimage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.showimage.R
import com.example.showimage.database.model.Image

class CollectionAdapter(
    private var context: Context,
    private var listImageData: List<Image>?
) : BaseAdapter() {
    public var contextAdapter: Context
        get() = this.context
        set(value) {
            this.context = value
        }
    public var listImage
        get() = listImageData as ArrayList<Image>
        set(value) {
            this.listImageData = value
            notifyDataSetChanged()
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var viewLayoutItem = inflater.inflate(R.layout.grid_item_layout, null)
        var items = this.listImage.get(position)
        var imgItem:ImageView = viewLayoutItem.findViewById(R.id.imageItem)
        var titleImage: TextView = viewLayoutItem.findViewById(R.id.titleImage)
        var url = "https://farm${items.farm}.staticflickr.com/${items.server}/${items.id}_${items.secret}.jpg"
        Glide.with(context).load(url).into(imgItem)
//        var url2 = "https://live.staticflickr.com/${items.server}/${items.id}_${items.secret}.jpg"
//        Glide.with(context).load(url2).into(imgItem)
        titleImage.setText(items.title)
        return viewLayoutItem
    }

    override fun getItem(position: Int): Any = this.listImageData!!.get(position)

    override fun getItemId(position: Int): Long = this.listImageData!!.get(position).id!!.toLong()

    override fun getCount(): Int = this.listImageData!!.size

    class CollectionViewHodler(
        var imageView: ImageView,
        var textView: TextView
    ) {
        public var imageViewHolder: ImageView
            get() = this.imageView
            set(value) {
                imageView = value
            }

    }
}