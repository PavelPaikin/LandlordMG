package com.dev.lakik.landlordmg.Adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.cloudinary.Transformation
import com.cloudinary.android.MediaManager
import com.dev.lakik.landlordmg.Extentions.inflate
import com.dev.lakik.landlordmg.Model.Property
import com.dev.lakik.landlordmg.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_item_property.view.*

class rvPropertyAdapter(private val properties: ArrayList<Property>) : RecyclerView.Adapter<rvPropertyAdapter.PropertyHolder>()  {

    public lateinit var onClickListener: (View, Property) -> Unit
    public lateinit var onLongClickListener: (View?, Property) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): rvPropertyAdapter.PropertyHolder{
        val inflatedView = parent!!.inflate(R.layout.rv_item_property, false)
        return PropertyHolder(inflatedView, onClickListener, onLongClickListener)
    }

    override fun getItemCount(): Int {
        return properties.count()
    }

    override fun onBindViewHolder(holder: rvPropertyAdapter.PropertyHolder, position: Int) {
        val item = properties[position]
        holder.bindProperty(item)
    }

    class PropertyHolder(v: View, onClickListener: (View, Property) -> Unit, onLongClickListener: (View?, Property) -> Unit) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {

        private var view: View = v
        private var property: Property? = null
        private var onClickListener: (View, Property) -> Unit = onClickListener
        private var onLongClickListener: (View?, Property) -> Unit = onLongClickListener

        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            onClickListener(v,  property!!)
        }

        override fun onLongClick(v: View?): Boolean {
            onLongClickListener(v, property!!)
            return true
        }


        fun bindProperty(property: Property) {
            this.property = property
            //Picasso.with(view.context).load(photo.url).into(view.itemImage)
            view.tvPropertyName.text = property.name
            view.tvPropertyAddress.text = property.address1

            if(property.images.isNotEmpty()) {
                var imgName =property.images + ".jpg"
                view.imgProperty.scaleType = ImageView.ScaleType.CENTER_CROP
                Picasso.get().load(MediaManager.get().url().transformation(Transformation<Transformation<out Transformation<*>>?>().width(256)!!.height(256).gravity("faces").crop("fill")).generate(imgName)).into(view.imgProperty)
            }
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }
    }
}