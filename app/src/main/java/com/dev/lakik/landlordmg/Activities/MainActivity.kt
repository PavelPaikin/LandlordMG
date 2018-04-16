package com.dev.lakik.landlordmg.Activities

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
import android.graphics.Rect
import android.widget.EditText
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager


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
        CreateOrEditLeaseFragment.OnFragmentInteractionListener{

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            /*val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
            val tag = currentFragment.tag*/

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

                        }
                    }
                }

            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            //updateUI()
        }

        nav_view.setNavigationItemSelectedListener(this)

        setDrawerToggle()
        setFragment(EnumFragments.PROPERTIES_FRAGMENT, null)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            //updateUI()
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
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

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
                        fab.visibility = VISIBLE
                    }
                    UnitTenantsFragment.TAG -> {
                        fab.visibility = VISIBLE
                    }
                    UnitDetailsFragment.TAG -> {
                        fab.visibility = INVISIBLE
                    }
                }
            }
        }
    }

    fun setDrawerToggle(){
        toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }



}
