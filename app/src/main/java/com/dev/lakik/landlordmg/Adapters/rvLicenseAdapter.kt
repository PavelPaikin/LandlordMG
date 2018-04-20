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
import kotlinx.android.synthetic.main.rv_item_license.view.*
import kotlinx.android.synthetic.main.rv_item_property.view.*
import kotlinx.android.synthetic.main.rv_item_unit.view.*

class rvLicenseAdapter() : RecyclerView.Adapter<rvLicenseAdapter.TenantHolder>()  {


    private var items: ArrayList<LicenseItem> = ArrayList()
    public lateinit var onClickListener: (View, LicenseItem) -> Unit

    init{
        var license1 = LicenseItem()
        license1.title = "Fuel"
        license1.description = "The easiest HTTP networking library for Kotlin/Android."
        license1.link = "https://github.com/kittinunf/Fuel"
        items.add(license1)

        var license2 = LicenseItem()
        license2.title = "google-gson"
        license2.description = "Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object. Gson can work with arbitrary Java objects including pre-existing objects that you do not have source-code of."
        license2.link = "https://github.com/google/gson"
        items.add(license2)

        var license3 = LicenseItem()
        license3.title = "Material DateTime Picker "
        license3.description = "Material DateTime Picker tries to offer you the date and time pickers as shown in the Material Design spec, with an easy themable API. The library uses the code from the Android frameworks as a base and tweaked it to be as close as possible to Material Design example."
        license3.link = "https://github.com/wdullaer/MaterialDateTimePicker"
        items.add(license3)

        var license4 = LicenseItem()
        license4.title = "CircleImageView"
        license4.description = "A fast circular ImageView perfect for profile images. This is based on RoundedImageView from Vince Mi which itself is based on techniques recommended by Romain Guy."
        license4.link = "https://github.com/hdodenhof/CircleImageView"
        items.add(license4)

        var license5 = LicenseItem()
        license5.title = "Cloudinary"
        license5.description = "Cloudinary is a cloud service that offers a solution to a web application's entire image management pipeline."
        license5.link = "https://github.com/cloudinary/cloudinary_android"
        items.add(license5)

        var license6 = LicenseItem()
        license6.title = "Picasso"
        license6.description = "A powerful image downloading and caching library for Android"
        license6.link = "https://github.com/square/picasso"
        items.add(license6)

        var license7 = LicenseItem()
        license7.title = "PatternedTextWatcher"
        license7.description = "Customizable TextWatcher for implementing different input types very quickly."
        license7.link = "https://github.com/zsavely/PatternedTextWatcher"
        items.add(license7)

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): rvLicenseAdapter.TenantHolder{
        val inflatedView = parent!!.inflate(R.layout.rv_item_license, false)
        return TenantHolder(inflatedView, onClickListener)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: rvLicenseAdapter.TenantHolder, position: Int) {
        val item = items[position]
        holder.bindItem(item)
    }

    class TenantHolder(v: View, onClickListener: (View, LicenseItem) -> Unit) : RecyclerView.ViewHolder(v), View.OnClickListener{

        private var view: View = v
        private var item: LicenseItem? = null
        private var onClickListener: (View, LicenseItem) -> Unit = onClickListener

        init {
            v.btnVisit.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when(v){
                view.btnVisit -> {
                    onClickListener(v,  item!!)
                }
            }
        }



        fun bindItem(item: LicenseItem) {
            this.item = item
            Picasso.get().load(item.img).into(view.imgLicense)
            view.tvTitle.text = item.title
            view.tvDesc.text = item.description
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }
    }

    public class LicenseItem{
        var img: Int = R.drawable.git_hub
        var title: String = ""
        var description: String = ""
        var link: String = ""
    }
}