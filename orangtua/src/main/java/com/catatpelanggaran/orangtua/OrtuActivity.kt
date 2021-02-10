package com.catatpelanggaran.orangtua

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.adriandery.catatpelanggaran.LoginActivity
import com.adriandery.catatpelanggaran.SharedPreferences
import com.catatpelanggaran.orangtua.dashboard.DashboardFragment
import com.catatpelanggaran.orangtua.profile.EditFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_ortu.*
import kotlinx.android.synthetic.main.app_bar_main.*

class OrtuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var nis: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ortu)

        nis = intent.getStringExtra("NIS").toString()
        getData(nis)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, DashboardFragment())
                .commit()
            supportActionBar?.title = "Dashboard"
        }

    }

    private fun getData(nis: String) {
        TODO("Not yet implemented")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        var title = "Dashboard"
        when (item.itemId) {
            R.id.nav_dashboard -> {
                fragment = DashboardFragment()
                title = "Dashbpard"
            }
            R.id.nav_edit -> {
                fragment = EditFragment()
                title = "Edit"
            }
            R.id.nav_logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                SharedPreferences.clearData(this)
                finish()
            }
        }
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()
        }
        supportActionBar?.title = title

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}