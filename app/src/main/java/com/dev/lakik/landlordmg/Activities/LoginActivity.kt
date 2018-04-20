package com.dev.lakik.landlordmg.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Fragments.Login.LoginFragment
import com.dev.lakik.landlordmg.Fragments.Login.RegistrationFragment
import com.dev.lakik.landlordmg.Model.User
import com.dev.lakik.landlordmg.R
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.Snackbar
import android.util.Log
import com.dev.lakik.landlordmg.Extentions.containsOnly
import com.dev.lakik.landlordmg.Extentions.requestPermission
import com.dev.lakik.landlordmg.Extentions.shouldShowPermissionRationale
import com.dev.lakik.landlordmg.Fragments.Main.CreateOrEditTenantFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_main.*


class LoginActivity : AppCompatActivity(),
                    LoginFragment.OnFragmentInteractionListener,
                    RegistrationFragment.OnFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setFragment(EnumFragments.LOGIN_FRAGMENT);
    }

    override fun setFragment(fragmentType: EnumFragments) {
        val transaction = supportFragmentManager.beginTransaction()
        var fragment: Fragment? = null
        when(fragmentType){
            EnumFragments.LOGIN_FRAGMENT -> {
                fragment = LoginFragment.newInstance(null)

            }
            EnumFragments.REGISTRATION_FRAGMENT -> {
                fragment = RegistrationFragment.newInstance(null)
                transaction.addToBackStack(null)
            }
        }
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.loginContainer, fragment)
        transaction.commit()
    }

    override fun openMainActivity(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    companion object {
        const val TAG = "LoginActivity"
        const val REQUEST_CAMERA = 0
    }

}
