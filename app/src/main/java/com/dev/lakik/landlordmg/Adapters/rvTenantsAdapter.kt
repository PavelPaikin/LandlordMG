package com.dev.lakik.landlordmg.Adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cloudinary.Transformation
import com.cloudinary.android.MediaManager
import com.dev.lakik.landlordmg.Extentions.inflate
import com.dev.lakik.landlordmg.Model.Property
import com.dev.lakik.landlordmg.Model.Tenant
import com.dev.lakik.landlordmg.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.lv_tenant_item.view.*
import kotlinx.android.synthetic.main.rv_item_property.view.*
import kotlinx.android.synthetic.main.rv_item_unit.view.*

class rvTenantsAdapter(private val tenants: ArrayList<Tenant>) : RecyclerView.Adapter<rvTenantsAdapter.TenantHolder>()  {

    public lateinit var onClickListener: (View, Tenant) -> Unit
    public lateinit var onPhoneClickListener: (View?, Tenant) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): rvTenantsAdapter.TenantHolder{
        val inflatedView = parent!!.inflate(R.layout.lv_tenant_item, false)
        return TenantHolder(inflatedView, onClickListener, onPhoneClickListener)
    }

    override fun getItemCount(): Int {
        return tenants.count()
    }

    override fun onBindViewHolder(holder: rvTenantsAdapter.TenantHolder, position: Int) {
        val item = tenants[position]
        holder.bindProperty(item)
    }

    class TenantHolder(v: View, onClickListener: (View, Tenant) -> Unit, onPhoneClickListener: (View?, Tenant) -> Unit) : RecyclerView.ViewHolder(v), View.OnClickListener{

        private var view: View = v
        private var tenant: Tenant? = null
        private var onClickListener: (View, Tenant) -> Unit = onClickListener
        private var onPhoneClickListener: (View?, Tenant) -> Unit = onPhoneClickListener

        init {
            v.setOnClickListener(this)
            v.imgProfile.setOnClickListener(this)
            v.tvName.setOnClickListener(this)
            v.tvNumber.setOnClickListener(this)
            v.imgPhone.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when(v){
                view, view.imgProfile, view.tvName, view.tvNumber -> {
                    onClickListener(v,  tenant!!)
                }
                v.imgPhone -> {
                    onPhoneClickListener(v, tenant!!)
                }
            }
        }



        fun bindProperty(tenant: Tenant) {
            this.tenant = tenant
            view.tvName.text = tenant.name
            if(tenant.phones != null && tenant.phones.count() > 0) {
                view.tvNumber.visibility = View.VISIBLE
                view.imgPhone.visibility = View.VISIBLE
                view.tvNumber.text = tenant.phones.first().title
            }else{
                view.tvNumber.visibility = View.GONE
                view.imgPhone.visibility = View.GONE
            }

            if(tenant.img.isNotEmpty()) {
                var imgName =tenant.img + ".jpg"
                Picasso.get().load(MediaManager.get().url().transformation(Transformation<Transformation<out Transformation<*>>?>().width(128)!!.height(128).gravity("faces").crop("fill")).generate(imgName)).into(view.imgProfile)
            }
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }
    }
}