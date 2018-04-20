package com.dev.lakik.landlordmg.Fragments.Main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.dev.lakik.landlordmg.Adapters.OnetimeChargeListAdapter
import com.dev.lakik.landlordmg.Common.Action
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData

import com.dev.lakik.landlordmg.R
import kotlin.collections.ArrayList
import android.view.ViewGroup
import com.dev.lakik.landlordmg.Adapters.SecuretyDepositListAdapter
import com.dev.lakik.landlordmg.Model.Tenant
import kotlinx.android.synthetic.main.fragment_create_or_edit_tenant.*
import android.graphics.Bitmap
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import com.cloudinary.Transformation
import com.cloudinary.android.MediaManager
import com.dev.lakik.landlordmg.Adapters.EmailsListAdapter
import com.dev.lakik.landlordmg.Adapters.PhonesListAdapter
import com.dev.lakik.landlordmg.Helpers.CaptureImageHelper
import com.dev.lakik.landlordmg.Helpers.ListViewHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_item_unit.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CreateOrEditTenantFragment : Fragment() {

    lateinit var imageHelper: CaptureImageHelper

    private val PICK_IMAGE_ID = 234 // the number doesn't matter
    var tenant: Tenant? = null
    var action: Action? = null
    var phones: ArrayList<Tenant.Phone>? = null
    var emails: ArrayList<Tenant.Email>? = null

    var phonesAdapter: PhonesListAdapter? = null
    var emailsAdapter: EmailsListAdapter? = null

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater!!.inflate(R.layout.fragment_create_or_edit_tenant, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener") as Throwable
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalData.mainActiveFragment = TAG
        listener!!.updateUI()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageHelper = CaptureImageHelper(activity!!, this)

        var tenantArg = arguments!!.get ("tenant")
        if( tenantArg != null) {
            tenant = tenantArg as Tenant
        }

        action = arguments!!.get("action") as Action

        if(action == Action.EDIT){
            setDataToForm()
        }else {
            tenant = Tenant()
            phones = ArrayList<Tenant.Phone>()
            emails = ArrayList<Tenant.Email>()
        }

        imgProfile.setOnClickListener {
            imageHelper.capture()
        }

        phonesAdapter = PhonesListAdapter(context!!, phones!!)
        lvPhones.adapter = phonesAdapter
        phonesAdapter!!.listView = lvPhones

        ListViewHelper.setListViewHeightBasedOnChildren(lvPhones)

        tvAddPhoneNumber.setOnClickListener {
            phonesAdapter!!.addEmptyRow()
        }

        emailsAdapter = EmailsListAdapter(context!!, emails!!)
        lvEmails.adapter = emailsAdapter
        emailsAdapter!!.listView = lvEmails

        ListViewHelper.setListViewHeightBasedOnChildren(lvEmails)

        tvAddEmailAddress.setOnClickListener {
            emailsAdapter!!.addEmptyRow()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.create_edit_propert_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

       when (item!!.itemId) {
           R.id.done -> {
               tenant!!.name = etTenantName.text.toString()
               tenant!!.phones = phonesAdapter!!.items
               tenant!!.emails = emailsAdapter!!.items

               pbLoading.visibility = View.VISIBLE
               llContent.alpha = 0.2f

               tenant!!.save ({
                   pbLoading.visibility = View.GONE
                   llContent.alpha = 1.0f
                   activity!!.onBackPressed()
               },{
                   pbLoading.visibility = View.GONE
                   llContent.alpha = 1.0f
               })
               return true
           }

           else -> {
               super.onOptionsItemSelected(item)
               return true
           }
       }
    }


    fun setDataToForm(){
        if(tenant!!.img.isNotEmpty()) {
            var imgName = tenant!!.img + ".jpg"
            Picasso.get().load(MediaManager.get().url().transformation(Transformation<Transformation<out Transformation<*>>?>().width(256)!!.height(256).gravity("faces").crop("fill")).generate(imgName)).placeholder(R.drawable.profile).into(imgProfile)
        }

        etTenantName.setText(tenant!!.name)

        phones = tenant!!.phones
        emails = tenant!!.emails
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        imageHelper.onActivityResult(requestCode, resultCode, data, { bitmap ->
            tenant!!.imageFile = imageHelper.photoFile
            Picasso.get().load(tenant!!.imageFile!!).into(imgProfile)
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        imageHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
        fun updateUI()
    }

    companion object {
        var TAG = "CreateOrEditTenantFragment"

        fun newInstance(args: Bundle?): CreateOrEditTenantFragment {
            val fragment = CreateOrEditTenantFragment()

            fragment.arguments = args
            return fragment
        }
    }
}


