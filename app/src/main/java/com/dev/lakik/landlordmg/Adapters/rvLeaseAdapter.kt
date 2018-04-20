package com.dev.lakik.landlordmg.Adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.cloudinary.Transformation
import com.cloudinary.android.MediaManager
import com.dev.lakik.landlordmg.Extentions.getStringByPattern
import com.dev.lakik.landlordmg.Extentions.inflate
import com.dev.lakik.landlordmg.Model.Lease
import com.dev.lakik.landlordmg.Model.Property
import com.dev.lakik.landlordmg.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_item_lease.view.*

class rvLeaseAdapter(private val items: ArrayList<Lease>) : RecyclerView.Adapter<rvLeaseAdapter.LeaseHolder>()  {

    public lateinit var onClickListener: (View, Lease) -> Unit
    public lateinit var onLongClickListener: (View?, Lease) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): rvLeaseAdapter.LeaseHolder{
        val inflatedView = parent!!.inflate(R.layout.rv_item_lease, false)
        return LeaseHolder(inflatedView, onClickListener, onLongClickListener)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: rvLeaseAdapter.LeaseHolder, position: Int) {
        val item = items[position]
        holder.bindProperty(item)
    }

    class LeaseHolder(v: View, onClickListener: (View, Lease) -> Unit, onLongClickListener: (View?, Lease) -> Unit) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {

        private var view: View = v
        private var lease: Lease? = null
        private var onClickListener: (View, Lease) -> Unit = onClickListener
        private var onLongClickListener: (View?, Lease) -> Unit = onLongClickListener

        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            onClickListener(v,  lease!!)
        }

        override fun onLongClick(v: View?): Boolean {
            onLongClickListener(v, lease!!)
            return true
        }


        fun bindProperty(lease: Lease) {
            this.lease = lease

            view.tvUnitName.text = lease.unitName
            if (lease.buildingName != null){
                view.tvBildingName.visibility = View.VISIBLE
                view.tvBildingName.text = lease.buildingName
            }else{
                view.tvBildingName.visibility = View.GONE
            }

            var dates = StringBuilder()
            dates.append(lease.startDate.getStringByPattern("dd MMM yy"))
            dates.append("\n-\n")
            dates.append(lease.endDate.getStringByPattern("dd MMM yy"))

            view.tvDates.text = dates

            if(!lease.img.isNullOrEmpty()) {
                var imgName = lease.img + ".jpg"
                view.imgProperty.scaleType = ImageView.ScaleType.CENTER_CROP
                Picasso.get().load(MediaManager.get().url().transformation(Transformation<Transformation<out Transformation<*>>?>().width(256)!!.height(256).gravity("faces").crop("fill")).generate(imgName)).into(view.imgProperty)
            }
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }
    }
}