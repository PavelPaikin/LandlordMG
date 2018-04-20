package com.dev.lakik.landlordmg.Fragments.Main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.dev.lakik.landlordmg.Common.Action
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData

import com.dev.lakik.landlordmg.R
import kotlin.collections.ArrayList
import android.view.ViewGroup
import com.dev.lakik.landlordmg.Model.Tenant
import com.cloudinary.Transformation
import com.cloudinary.android.MediaManager
import com.dev.lakik.landlordmg.Adapters.*
import com.dev.lakik.landlordmg.Helpers.CallHelper
import com.dev.lakik.landlordmg.Helpers.ListViewHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_view_tenant.*


class ViewTenantFragment : Fragment() {

    lateinit var callHelper: CallHelper

    var tenant: Tenant? = null
    var phones: ArrayList<Tenant.Phone>? = null
    var emails: ArrayList<Tenant.Email>? = null

    var phonesAdapter: PhonesListViewAdapter? = null
    var emailsAdapter: EmailsListViewAdapter? = null

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater!!.inflate(R.layout.fragment_view_tenant, container, false)
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

        callHelper = CallHelper(activity!!, this)

        var tenantArg = arguments!!.get ("tenant")
        if( tenantArg != null) {
            tenant = tenantArg as Tenant
        }

        setDataToForm()

        phonesAdapter = PhonesListViewAdapter(context!!, phones!!)
        lvPhones.adapter = phonesAdapter
        ListViewHelper.setListViewHeightBasedOnChildren(lvPhones)

        lvPhones.setOnItemClickListener { parent, view, position, id ->
            callHelper.makeCall(phones!![position].title)
        }

        emailsAdapter = EmailsListViewAdapter(context!!, emails!!)
        lvEmails.adapter = emailsAdapter
        ListViewHelper.setListViewHeightBasedOnChildren(lvEmails)

        lvEmails.setOnItemClickListener { parent, view, position, id ->
            val to = emails!![position].title

            val intent = Intent(Intent.ACTION_SEND)
            val addressees = arrayOf(to)
            intent.putExtra(Intent.EXTRA_EMAIL, addressees)
            intent.setType("message/rfc822")
            startActivity(Intent.createChooser(intent, "Send Email using:"));
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.property_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

       when (item!!.itemId) {
           R.id.edit -> {
               val args = Bundle()
               args.putSerializable("tenant", tenant)
               args.putSerializable("action", Action.EDIT)

               listener!!.setFragment(EnumFragments.CREATE_OR_EDIT_TENANT_FRAGMENT, args)
               return true
           }
            R.id.delete ->{
                tenant!!.delete {
                    activity!!.onBackPressed()
                }
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
        tvTenantName.text = tenant!!.name

        phones = tenant!!.phones
        emails = tenant!!.emails
    }


    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
        fun updateUI()
    }

    companion object {
        var TAG = "ViewTenantFragment"

        fun newInstance(args: Bundle?): ViewTenantFragment {
            val fragment = ViewTenantFragment()

            fragment.arguments = args
            return fragment
        }
    }
}


