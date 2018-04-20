package com.dev.lakik.landlordmg.Fragments.Main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import com.cloudinary.Transformation
import com.cloudinary.android.MediaManager
import com.dev.lakik.landlordmg.Common.Action
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Extentions.boolean
import com.dev.lakik.landlordmg.Extentions.int
import com.dev.lakik.landlordmg.Helpers.CaptureImageHelper
import com.dev.lakik.landlordmg.Model.Property
import com.dev.lakik.landlordmg.Model.User

import com.dev.lakik.landlordmg.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_create_or_edit_property.*


class CreateOrEditPropertyFragment : Fragment() {

    lateinit var imageHelper: CaptureImageHelper

    var property: Property? = null
    var action: Action? = null

    lateinit var typeSelected: Property.PropertyType

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater!!.inflate(R.layout.fragment_create_or_edit_property, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
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

        var propertyArg = arguments!!.get("property")
        if( propertyArg != null) {
            property = propertyArg as Property
        }

        action = arguments!!.get("action") as Action
        if((action == Action.CREATE && property != null && property!!.type == Property.PropertyType.MULTI_UNIT) || (action == Action.EDIT)){
            llTypeSelect.visibility = View.GONE
        }

        rgPropertyType.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener {
            group, checkedId ->

            ChangeForm(checkedId, false)
        }))

        if(action == Action.EDIT){


            setDataToForm()
        }else {
            ChangeForm(R.id.rbSingleUnit, false)
            typeSelected = Property.PropertyType.SINGLE_UNIT
        }

        imgProperty.setOnClickListener {
            imageHelper.capture()
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
              //Toast.makeText(context, "DONE", Toast.LENGTH_SHORT).show()
               var perentIdString: String? = null
               var address1String = ""
               var address2String = ""
               var cityString = ""
               var provinceString = ""
               var postalCodeString = ""
               var rooms = 0
               var bath = 0

              if(action == Action.CREATE){
                  if(property != null && property!!.type == Property.PropertyType.MULTI_UNIT){
                      address1String = property!!.address1
                      address2String = property!!.address2
                      cityString = property!!.city
                      provinceString = property!!.province
                      postalCodeString = property!!.postalCode

                      perentIdString = property!!.id
                  }else{
                      address1String = etAddress1.text.toString()
                      address2String = etAddress2.text.toString()
                      cityString = etCity.text.toString()
                      provinceString = etProvince.text.toString()
                      postalCodeString = etPostalCode.text.toString()

                  }

                  if(typeSelected == Property.PropertyType.SINGLE_UNIT){
                      var roomString = etRooms.text.toString()
                      if(!roomString.isEmpty())  rooms = Integer.parseInt(roomString)

                      var bathString = etBath.text.toString()
                      if(!bathString.isEmpty()) bath = Integer.parseInt(bathString)
                  }

                  var newProperty = Property(
                          id = "",
                          parentId = perentIdString,
                          userId = User.instance!!.id,
                          type = typeSelected,
                          name = etName.text.toString(),
                          address1 = address1String,
                          address2 = address2String,
                          city = cityString,
                          province = provinceString,
                          postalCode = postalCodeString,
                          rooms = rooms,
                          bathrooms = bath,
                          notes = etNotes.text.toString(),
                          furneture = cbFurnished.isChecked.int,
                          petFriendly = cbPetFriendly.isChecked.int,
                          images =  ""
                  )
                  newProperty.imageFile = imageHelper.photoFile
                  pbLoading.visibility = View.VISIBLE
                  container.alpha = 0.2f
                  newProperty.save {
                      pbLoading.visibility = View.GONE
                      container.alpha = 1.0f
                      if(property != null && property!!.type == Property.PropertyType.MULTI_UNIT){
                          activity!!.onBackPressed()
                      }else{
                          listener!!.setFragment(EnumFragments.PROPERTIES_FRAGMENT, null)
                      }

                  }
              }else{
                   property!!.name = etName.text.toString()
                   property!!.notes = etNotes.text.toString()

                   when(property!!.type){
                       Property.PropertyType.SINGLE_UNIT ->{
                           if(property!!.parentId == null){
                               property!!.address1 = etAddress1.text.toString()
                               property!!.address2 = etAddress2.text.toString()
                               property!!.city = etCity.text.toString()
                               property!!.province = etProvince.text.toString()
                               property!!.postalCode = etPostalCode.text.toString()
                           }

                           var roomString = etRooms.text.toString()
                           if(!roomString.isEmpty())  property!!.rooms = Integer.parseInt(roomString)

                           var bathString = etBath.text.toString()
                           if(!bathString.isEmpty()) property!!.bathrooms = Integer.parseInt(bathString)

                           property!!.furneture = cbFurnished.isChecked.int
                           property!!.petFriendly = cbPetFriendly.isChecked.int

                       }
                       Property.PropertyType.MULTI_UNIT ->{
                           property!!.address1 = etAddress1.text.toString()
                           property!!.address2 = etAddress2.text.toString()
                           property!!.city = etCity.text.toString()
                           property!!.province = etProvince.text.toString()
                           property!!.postalCode = etPostalCode.text.toString()
                       }
                   }

                  pbLoading.visibility = View.VISIBLE
                  container.alpha = 0.2f
                  property!!.imageFile = imageHelper.photoFile
                  property!!.save {

                      pbLoading.visibility = View.GONE
                      container.alpha = 1.0f
                      activity!!.onBackPressed()
                  }
              }

               return true
           }

           else -> {
               super.onOptionsItemSelected(item)
               return true
           }
       }
    }

    fun ChangeForm(type: Int, withAnimation: Boolean){
        HideAllFields()

        when (type) {
            R.id.rbSingleUnit -> {
                if(property == null || (action == Action.EDIT && property!!.parentId == null)) {
                    etWrapperAddress1.visibility = View.VISIBLE
                    etWrapperAddress2.visibility = View.VISIBLE
                    etWrapperCity.visibility = View.VISIBLE
                    etWrapperProvince.visibility = View.VISIBLE
                    etWrapperPostalCode.visibility = View.VISIBLE
                }

                etWrapperName.visibility = View.VISIBLE
                etWrapperRooms.visibility = View.VISIBLE
                etWrapperBath.visibility = View.VISIBLE
                etWrapperNotes.visibility = View.VISIBLE

                llAdditionalInfoSection.visibility = View.VISIBLE

                typeSelected = Property.PropertyType.SINGLE_UNIT

            }
            R.id.rbMultiUnit -> {
                etWrapperName.visibility = View.VISIBLE
                etWrapperAddress1.visibility = View.VISIBLE
                etWrapperAddress2.visibility = View.VISIBLE
                etWrapperCity.visibility = View.VISIBLE
                etWrapperProvince.visibility = View.VISIBLE
                etWrapperPostalCode.visibility = View.VISIBLE
                etWrapperNotes.visibility = View.VISIBLE

                typeSelected = Property.PropertyType.MULTI_UNIT
            }
        }

        etName.requestFocus()
    }

    fun HideAllFields(){
        etWrapperName.visibility = View.GONE
        etWrapperAddress1.visibility = View.GONE
        etWrapperAddress2.visibility = View.GONE
        etWrapperCity.visibility = View.GONE
        etWrapperProvince.visibility = View.GONE
        etWrapperPostalCode.visibility = View.GONE
        etWrapperRooms.visibility = View.GONE
        etWrapperBath.visibility = View.GONE
        etWrapperNotes.visibility = View.GONE

        llAdditionalInfoSection.visibility = View.GONE
    }

    fun setDataToForm(){
        if(property!!.images.isNotEmpty()) {
            var imgName = property!!.images + ".jpg"
            imgProperty.scaleType = ImageView.ScaleType.CENTER_CROP
            Picasso.get().load(MediaManager.get().url().transformation(Transformation<Transformation<out Transformation<*>>?>().width(500)!!.height(500)).generate(imgName)).resize(500, 500).centerCrop().into(imgProperty)
        }
        etName.setText(property!!.name)
        etNotes.setText(property!!.notes)

        when(property!!.type){
            Property.PropertyType.SINGLE_UNIT -> {
                etRooms.setText(property!!.rooms.toString())
                etBath.setText(property!!.bathrooms.toString())
                cbFurnished.isChecked = property!!.furneture.boolean
                cbPetFriendly.isChecked = property!!.petFriendly.boolean
                if(property!!.parentId == null){
                    etAddress1.setText(property!!.address1)
                    etAddress2.setText(property!!.address2)
                    etCity.setText(property!!.city)
                    etProvince.setText(property!!.province)
                    etPostalCode.setText(property!!.postalCode)
                }

                ChangeForm(R.id.rbSingleUnit, false)
                typeSelected = Property.PropertyType.SINGLE_UNIT
            }
            Property.PropertyType.MULTI_UNIT -> {
                etAddress1.setText(property!!.address1)
                etAddress2.setText(property!!.address2)
                etCity.setText(property!!.city)
                etProvince.setText(property!!.province)
                etPostalCode.setText(property!!.postalCode)

                ChangeForm(R.id.rbMultiUnit, false)
                typeSelected = Property.PropertyType.MULTI_UNIT
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        imageHelper.onActivityResult(requestCode, resultCode, data, { bitmap ->
            //property!!.imageFile = imageHelper.photoFile
            imgProperty.scaleType = ImageView.ScaleType.CENTER_CROP
            Picasso.get().load(imageHelper.photoFile!!).resize(500, 500).centerCrop().into(imgProperty)
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
        var TAG = "CreateOrEditPropertyFragment"

        fun newInstance(args: Bundle?): CreateOrEditPropertyFragment {
            val fragment = CreateOrEditPropertyFragment()

            fragment.arguments = args
            return fragment
        }
    }
}
