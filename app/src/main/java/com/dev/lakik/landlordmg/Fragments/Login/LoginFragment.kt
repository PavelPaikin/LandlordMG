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
import com.dev.lakik.landlordmg.Extentions.ValidEmail
import com.dev.lakik.landlordmg.Extentions.ValidPassword
import com.dev.lakik.landlordmg.Extentions.hideKeyboard
import com.dev.lakik.landlordmg.Helpers.myCrypt
import com.dev.lakik.landlordmg.Model.User

import com.dev.lakik.landlordmg.R
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject

class LoginFragment : Fragment() {

    companion object {
        fun newInstance(arg: Bundle?) : LoginFragment {
            return LoginFragment()
        }
    }

    private var mListener: OnFragmentInteractionListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvSignUp.setOnClickListener(View.OnClickListener {
          mListener!!.setFragment(EnumFragments.REGISTRATION_FRAGMENT)
        })

        btnSignIn.setOnClickListener(View.OnClickListener {

            etLoginEmail.error = null
            etLoginPassword.error = null

            val email = etLoginEmail.text.toString()
            val pass = etLoginPassword.text.toString()

            formValidate(email, pass, {
                var crypt = myCrypt()
                hideKeyboard()
                val json = JSONObject()
                json.put("email", email)
                json.put("password", crypt.encrypt(pass))

                Fuel.post(URLS.API_LOGIN).body(json.toString()).response { request, response, result ->
                    val (data, error) = result
                    if (error == null) {
                        Log.d("ddd", String(data!!))

                        if(String(data!!).equals("null")){
                            Snackbar.make(view!!, R.string.wrong_email_or_password, Snackbar.LENGTH_LONG).show()
                        }else{
                            User.setUser(String(data!!))
                            if(User.instance != null) {
                                mListener!!.openMainActivity()
                            }else{
                                Snackbar.make(view!!, R.string.something_wrong, Snackbar.LENGTH_SHORT).show()
                            }
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

    fun formValidate(email: String, pass: String, valid: () -> Unit, invalid: (EditText) -> Unit){
        if(!email.ValidEmail()){
            etLoginEmail.error = getString(R.string.error_wrong_email_format)
            invalid(etLoginEmail)
            return
        }

        if(!pass.ValidPassword()){
            etLoginPassword.error = getString(R.string.error_wrong_email_format)
            invalid(etLoginPassword)
            return
        }

        valid()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_login, container, false)
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
