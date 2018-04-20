package com.dev.lakik.landlordmg.Activities

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import com.dev.lakik.landlordmg.Common.Action
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Common.GlobalData
import com.dev.lakik.landlordmg.Fragments.Main.*
import com.dev.lakik.landlordmg.Fragments.Property.PropertyDetailsFragment
import com.dev.lakik.landlordmg.Fragments.Property.PropertyUnitsFragment
import com.dev.lakik.landlordmg.Fragments.Unit.UnitDetailsFragment
import com.dev.lakik.landlordmg.Fragments.Unit.UnitLeaseFragment
import com.dev.lakik.landlordmg.Fragments.Unit.UnitTenantsFragment
import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Rect
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.EditText
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.cloudinary.android.MediaManager
import com.dev.lakik.landlordmg.Extentions.containsOnly
import com.dev.lakik.landlordmg.Extentions.requestPermission
import com.dev.lakik.landlordmg.Extentions.shouldShowPermissionRationale
import com.dev.lakik.landlordmg.Fragments.Leases.ActiveLeases
import com.dev.lakik.landlordmg.Fragments.Leases.ClosedLeases
import com.dev.lakik.landlordmg.Model.User
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        PropertiesFragment.OnFragmentInteractionListener,
        CreateOrEditPropertyFragment.OnFragmentInteractionListener,
        PropertyFragment.OnFragmentInteractionListener,
        PropertyUnitsFragment.OnFragmentInteractionListener,
        PropertyDetailsFragment.OnFragmentInteractionListener,
        UnitFragment.OnFragmentInteractionListener,
        UnitLeaseFragment.OnFragmentInteractionListener,
        UnitTenantsFragment.OnFragmentInteractionListener,
        UnitDetailsFragment.OnFragmentInteractionListener,
        CreateOrEditLeaseFragment.OnFragmentInteractionListener,
        CreateOrEditTenantFragment.OnFragmentInteractionListener,
        ViewTenantFragment.OnFragmentInteractionListener,
        LeasesFragment.OnFragmentInteractionListener,
        ActiveLeases.OnFragmentInteractionListener,
        ClosedLeases.OnFragmentInteractionListener,
        ViewClosedLeaseFragment.OnFragmentInteractionListener,
        ViewAllTenantsFragment.OnFragmentInteractionListener,
        LicenseFragment.OnFragmentInteractionListener{

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        try {
            MediaManager.init(this);
        }catch (ex: Exception){}

        if(User.instance == null){
            User.loadFromFile(this)
        }


        fab.setOnClickListener { view ->

            when (GlobalData.mainActiveFragment) {
                PropertiesFragment.TAG -> {
                    val args = Bundle()

                    args.putSerializable("action", Action.CREATE)

                    setFragment(EnumFragments.CREATE_OR_EDIT_PROPERTY_FRAGMENT, args)
                }

                PropertyFragment.TAG -> {
                    when(GlobalData.subActiveFragment) {
                        PropertyUnitsFragment.TAG -> {
                            val args = Bundle()
                            if (GlobalData.selectedProperty != null) {
                                args.putSerializable("property", GlobalData.selectedProperty)
                            }

                            args.putSerializable("action", Action.CREATE)

                            setFragment(EnumFragments.CREATE_OR_EDIT_PROPERTY_FRAGMENT, args)
                        }
                    }
                }
                UnitFragment.TAG -> {
                    when(GlobalData.subActiveFragment) {
                        UnitLeaseFragment.TAG -> {
                            val args = Bundle()

                            args.putSerializable("action", Action.CREATE)

                            setFragment(EnumFragments.CREATE_OR_EDIT_LEASE_FRAGMENT, args)
                        }
                        UnitTenantsFragment.TAG -> {
                            val args = Bundle()

                            args.putSerializable("action", Action.CREATE)

                            setFragment(EnumFragments.CREATE_OR_EDIT_TENANT_FRAGMENT, args)
                        }
                    }
                }

            }
        }

        nav_view.setNavigationItemSelectedListener(this)

        setDrawerToggle()
        setFragment(EnumFragments.PROPERTIES_FRAGMENT, null)

    }

    override fun onStop() {
        User.saveToFile(this)
        super.onStop()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_properties -> {
                setFragment(EnumFragments.PROPERTIES_FRAGMENT, null)
            }
            R.id.nav_leases -> {
                setFragment(EnumFragments.LEASES_FRAGMENT, null)
            }
            R.id.nav_tenants -> {
                setFragment(EnumFragments.VIEW_ALL_TENANTS_FRAGMENT, null)
            }
            R.id.nav_license ->{
                setFragment(EnumFragments.LICENSE_FRAGMENT, null)
            }
            R.id.nav_logout -> {
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun setFragment(fragmentType: EnumFragments, args: Bundle?) {
        val transaction = supportFragmentManager.beginTransaction()
        var fragment: Fragment? = null
        when(fragmentType){
            EnumFragments.PROPERTIES_FRAGMENT -> {
                fragment = PropertiesFragment.newInstance(args)
            }
            EnumFragments.CREATE_OR_EDIT_PROPERTY_FRAGMENT -> {
                fragment = CreateOrEditPropertyFragment.newInstance(args)

                transaction.addToBackStack(null)
            }
            EnumFragments.CREATE_OR_EDIT_LEASE_FRAGMENT -> {
                fragment = CreateOrEditLeaseFragment.newInstance(args)

                transaction.addToBackStack(null)
            }
            EnumFragments.PROPERTY_PRAGMENT -> {
                fragment = PropertyFragment.newInstance(args)

                transaction.addToBackStack(null)
            }
            EnumFragments.UNIT_FRAGMENT -> {
                fragment = UnitFragment.newInstance(args)

                transaction.addToBackStack(null)
            }
            EnumFragments.CREATE_OR_EDIT_TENANT_FRAGMENT -> {
                fragment = CreateOrEditTenantFragment.newInstance(args)

                transaction.addToBackStack(null)
            }
            EnumFragments.VIEW_TENANT_FRAGMENT -> {
                fragment = ViewTenantFragment.newInstance(args)

                transaction.addToBackStack(null)
            }
            EnumFragments.LEASES_FRAGMENT -> {
                fragment = LeasesFragment.newInstance(args)

                transaction.addToBackStack(null)
            }
            EnumFragments.VIEW_CLOSED_LEASE_FRAGMENT -> {
                fragment = ViewClosedLeaseFragment.newInstance(args)

                transaction.addToBackStack(null)
            }
            EnumFragments.VIEW_ALL_TENANTS_FRAGMENT -> {
                fragment = ViewAllTenantsFragment.newInstance(args)

                transaction.addToBackStack(null)
            }
            EnumFragments.LICENSE_FRAGMENT -> {
                fragment = LicenseFragment.newInstance(args)

                transaction.addToBackStack(null)
            }
        }

        transaction.replace(R.id.mainContainer, fragment,  GlobalData.mainActiveFragment)
        transaction.commit()
    }

    override fun updateUI(){
        when (GlobalData.mainActiveFragment) {
            PropertiesFragment.TAG -> {
                title = "Properties"
                toggle.isDrawerIndicatorEnabled = true
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                supportActionBar!!.setHomeButtonEnabled(true)
                setDrawerToggle()

                fab.visibility = VISIBLE
                tab_layout.visibility = GONE
            }
            CreateOrEditPropertyFragment.TAG -> {
                title = "Create Property"
                toggle.isDrawerIndicatorEnabled = false
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)

                toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }

                fab.visibility = INVISIBLE
                tab_layout.visibility = GONE
            }
            CreateOrEditLeaseFragment.TAG -> {
                title = "Create Lease"
                toggle.isDrawerIndicatorEnabled = false
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)

                toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }

                fab.visibility = INVISIBLE
                tab_layout.visibility = GONE
            }
            CreateOrEditTenantFragment.TAG -> {
                title = ""
                toggle.isDrawerIndicatorEnabled = false
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)

                toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }

                fab.visibility = INVISIBLE
                tab_layout.visibility = GONE
            }
            ViewTenantFragment.TAG -> {
                title = ""
                toggle.isDrawerIndicatorEnabled = false
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)

                toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }

                fab.visibility = INVISIBLE
                tab_layout.visibility = GONE
            }
            ViewClosedLeaseFragment.TAG -> {
                title = ""
                toggle.isDrawerIndicatorEnabled = false
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)

                toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }

                fab.visibility = INVISIBLE
                tab_layout.visibility = GONE
            }
            ViewAllTenantsFragment.TAG -> {
            title = "Tenants"
            toggle.isDrawerIndicatorEnabled = true
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setHomeButtonEnabled(true)
            setDrawerToggle()

            fab.visibility = INVISIBLE
            tab_layout.visibility = GONE
            }
            LicenseFragment.TAG -> {
                title = "License"
                toggle.isDrawerIndicatorEnabled = true
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                supportActionBar!!.setHomeButtonEnabled(true)
                setDrawerToggle()

                fab.visibility = INVISIBLE
                tab_layout.visibility = GONE
            }
            PropertyFragment.TAG -> {
                toggle.isDrawerIndicatorEnabled = true
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                supportActionBar!!.setHomeButtonEnabled(true)
                setDrawerToggle()

                tab_layout.visibility = VISIBLE

                title = GlobalData.selectedProperty!!.name

                when(GlobalData.subActiveFragment){
                    PropertyUnitsFragment.TAG -> {
                        fab.visibility = VISIBLE
                    }

                    PropertyDetailsFragment.TAG -> {
                        fab.visibility = INVISIBLE
                    }
                }
            }
            UnitFragment.TAG -> {
                title = GlobalData.selectedProperty!!.name
                toggle.isDrawerIndicatorEnabled = true
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                supportActionBar!!.setHomeButtonEnabled(true)
                setDrawerToggle()

                tab_layout.visibility = VISIBLE

                when(GlobalData.subActiveFragment){
                    UnitLeaseFragment.TAG -> {
                        if(GlobalData.selectedProperty!!.lease == null) {
                            fab.visibility = VISIBLE
                        }else{
                            fab.visibility = INVISIBLE
                        }
                    }
                    UnitTenantsFragment.TAG -> {
                        fab.visibility = VISIBLE
                    }
                    UnitDetailsFragment.TAG -> {
                        fab.visibility = INVISIBLE
                    }
                }
            }
            LeasesFragment.TAG -> {
                toggle.isDrawerIndicatorEnabled = true
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                supportActionBar!!.setHomeButtonEnabled(true)
                setDrawerToggle()

                tab_layout.visibility = VISIBLE

                title = "Leases"

                when(GlobalData.subActiveFragment){
                    ActiveLeases.TAG -> {
                        fab.visibility = INVISIBLE
                    }

                    ClosedLeases.TAG -> {
                        fab.visibility = INVISIBLE
                    }
                }
            }
        }
    }

    override fun setFABVisible(isVisible: Boolean){
        if(isVisible){
            fab.visibility = View.VISIBLE
        }else{
            fab.visibility = View.INVISIBLE
        }
    }

    fun setDrawerToggle(){
        toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }


    ///////////////////////////////////////////


}
