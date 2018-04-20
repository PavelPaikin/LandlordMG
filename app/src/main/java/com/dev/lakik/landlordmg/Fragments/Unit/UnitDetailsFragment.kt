package com.dev.lakik.landlordmg.Fragments.Unit

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ImageView
import com.cloudinary.Transformation
import com.cloudinary.android.MediaManager
import com.dev.lakik.landlordmg.Common.Action
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Model.Property

import com.dev.lakik.landlordmg.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_unit_details.*

class UnitDetailsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    lateinit var property: Property

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_unit_details, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        property = GlobalData.selectedProperty!!

        if(property!!.images.isNotEmpty()) {
            var imgName = property!!.images + ".jpg"
            imgProperty.scaleType = ImageView.ScaleType.CENTER_CROP
            Picasso.get().load(MediaManager.get().url().transformation(Transformation<Transformation<out Transformation<*>>?>().width(500)!!.height(500)).generate(imgName)).into(imgProperty)
        }

        tvName.text = property.name
        if(property.notes.isNotEmpty()){
            tvNotes.text = property.notes
        }else{
            tvNotes.visibility = View.GONE
        }

        tvAddress.text = property.address1 + " " + property.address2
        tvCityStatePostalCode.text = property.postalCode + ", " + property.city + ", " + property.province
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
                if (GlobalData.selectedProperty != null) {
                    args.putSerializable("property", GlobalData.selectedProperty)
                }

                args.putSerializable("action", Action.EDIT)

                listener!!.setFragment(EnumFragments.CREATE_OR_EDIT_PROPERTY_FRAGMENT, args)
                return true
            }
            R.id.delete -> {
                property!!.delete {
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

    interface OnFragmentInteractionListener {
        fun setFragment(fragment: EnumFragments, args: Bundle?)
        fun updateUI()
    }

    companion object {
        var TAG = "UnitDetailsFragment"

        fun newInstance(args: Bundle?): UnitDetailsFragment {
            val fragment = UnitDetailsFragment()

            fragment.arguments = args
            return fragment
        }
    }
}
