package com.dev.lakik.landlordmg.Fragments.Login

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.URLS
import com.dev.lakik.landlordmg.Extentions.*
import com.dev.lakik.landlordmg.Model.User

import com.dev.lakik.landlordmg.R
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.fragment_registration.*
import org.json.JSONObject

class RegistrationFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    companion object {
        fun newInstance(arg: Bundle?) : RegistrationFragment {
            return RegistrationFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvSignUp.setOnClickListener(View.OnClickListener {
            mListener!!.setFragment(EnumFragments.LOGIN_FRAGMENT)
        })

        registration_button.setOnClickListener(View.OnClickListener {

            ed_email.error = null
            ed_name.error = null
            ed_password.error = null
            ed_repassword.error = null

            val email = ed_email.text.toString()
            val name = ed_name.text.toString()
            val pass = ed_password.text.toString()
            val repass = ed_repassword.text.toString()

            formValidate(email, name, pass, repass, {

                hideKeyboard()
                val json = JSONObject()
                json.put("email", email)
                json.put("name", name)
                json.put("password", pass)

                Fuel.post(URLS.API_SIGN_UP).body(json.toString()).response { request, response, result ->
                    val (data, error) = result
                    if ((error == null) && (data!=null)) {
                        User.setUser(String(data!!))
                        if(User.instance != null) {
                            mListener!!.openMainActivity()
                        }else{
                            Snackbar.make(view!!, R.string.something_wrong, Snackbar.LENGTH_SHORT).show()
                        }
                    } else {
                        Snackbar.make(view!!, R.string.bad_request, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }, { errorView ->
                errorView.requestFocus()
            })

        })
    }

    fun formValidate(email: String, name: String, pass: String, rePass: String, valid: () -> Unit, invalid: (EditText) -> Unit ){

        if(!email.ValidEmail()){
            ed_email.error = getString(R.string.error_wrong_email_format)
            invalid(ed_email)
            return
        }

        if(!name.ValidUserName()){
            ed_name.error = getString(R.string.error_cant_be_empty)
            invalid(ed_name)
            return
        }

        if(!pass.ValidPassword()){
            ed_password.error = getString(R.string.error_wrong_password_format)
            invalid(ed_password)
            return
        }

        if(!rePass.ValidRePassword(pass)){
            ed_repassword.error = getString(R.string.error_wrong_repass)
            invalid(ed_repassword)
            return
        }

        valid()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }



    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
        fun setFragment(fragment: EnumFragments)
        fun openMainActivity()
    }
}
