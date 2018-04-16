package com.dev.lakik.landlordmg.Adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dev.lakik.landlordmg.Extentions.inflate
import com.dev.lakik.landlordmg.Model.Property
import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.rv_item_property.view.*
import kotlinx.android.synthetic.main.rv_item_unit.view.*

class rvUnitsAdapter(private val properties: ArrayList<Property>) : RecyclerView.Adapter<rvUnitsAdapter.UnitsHolder>()  {

    public lateinit var onClickListener: (View, Property) -> Unit
    public lateinit var onLongClickListener: (View?, Property) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): rvUnitsAdapter.UnitsHolder{
        val inflatedView = parent!!.inflate(R.layout.rv_item_unit, false)
        return UnitsHolder(inflatedView, onClickListener, onLongClickListener)
    }

    override fun getItemCount(): Int {
        return properties.count()
    }

    override fun onBindViewHolder(holder: rvUnitsAdapter.UnitsHolder, position: Int) {
        val item = properties[position]
        holder.bindProperty(item)
    }

    class UnitsHolder(v: View, onClickListener: (View, Property) -> Unit, onLongClickListener: (View?, Property) -> Unit) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {

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
            view.tvUnitName.text = property.name
            view.tvTenantName.text = ""
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }
    }
}