package com.dev.lakik.landlordmg.Activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import android.widget.Toast
import com.dev.lakik.landlordmg.Common.Action
import com.dev.lakik.landlordmg.Common.EnumFragments
import com.dev.lakik.landlordmg.Fragments.Main.CreateOrEditPropertyFragment
import com.dev.lakik.landlordmg.Fragments.Main.PropertiesFragment
import com.dev.lakik.landlordmg.Fragments.Main.PropertyFragment
import com.dev.lakik.landlordmg.Model.Property
import com.dev.lakik.landlordmg.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        PropertiesFragment.OnFragmentInteractionListener,
        CreateOrEditPropertyFragment.OnFragmentInteractionListener,
        PropertyFragment.OnFragmentInteractionListener{

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
            val tag = currentFragment.tag

            when (tag) {
                PropertiesFragment.TAG -> {

                    val args = Bundle()
                    /*if(property != null) {
                        args.putSerializable("property", property)
                    }*/

                    args.putSerializable("action", Action.CREATE)

                    setFragment(EnumFragments.CREATE_OR_EDIT_PROPERTY_FRAGMENT, args)
                }
            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            updateUI()
        }

        nav_view.setNavigationItemSelectedListener(this)

        setDrawerToggle()
        setFragment(EnumFragments.PROPERTIES_FRAGMENT, null)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            updateUI()
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

    override fun setFragment(fragmentType: EnumFragments, args: Bundle?) {
        val transaction = supportFragmentManager.beginTransaction()
        var fragment: Fragment? = null
        var tag: String? = null
        when(fragmentType){
            EnumFragments.PROPERTIES_FRAGMENT -> {
                fragment = PropertiesFragment.newInstance(args)
                tag = PropertiesFragment.TAG

            }
            EnumFragments.CREATE_OR_EDIT_PROPERTY_FRAGMENT -> {
                fragment = CreateOrEditPropertyFragment.newInstance(args)
                tag = CreateOrEditPropertyFragment.TAG

                transaction.addToBackStack(null)
            }
            EnumFragments.PROPERTY_PRAGMENT -> {
                fragment = PropertyFragment.newInstance(args)
                tag = PropertyFragment.TAG

                transaction.addToBackStack(null)
            }
        }

        transaction.replace(R.id.mainContainer, fragment,  tag)
        transaction.commit()

        updateUI(tag)
    }

    fun updateUI(tag: String? = null){
        var inTag = tag
        if(inTag == null){
            val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
            inTag = currentFragment.tag
        }

        when (inTag) {
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
            PropertyFragment.TAG -> {
                toggle.isDrawerIndicatorEnabled = true
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                supportActionBar!!.setHomeButtonEnabled(true)
                setDrawerToggle()

                fab.visibility = INVISIBLE
                tab_layout.visibility = VISIBLE

                tab_layout.removeAllTabs()
                tab_layout.addTab(tab_layout.newTab().setText("Units"))
                tab_layout.addTab(tab_layout.newTab().setText("Details"))
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