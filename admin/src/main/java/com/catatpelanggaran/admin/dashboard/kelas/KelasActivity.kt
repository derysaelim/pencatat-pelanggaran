package com.catatpelanggaran.admin.dashboard.kelas

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Kelas
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_kelas.*

class KelasActivity : AppCompatActivity() {

    var listKelas: ArrayList<Kelas>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelas)
        setSupportActionBar(toolbar_kelas)

        back_button.setOnClickListener { finish() }

        getData(null)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search_bar).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Cari"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                getData(query)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
//                getData(query)
                return true
            }

        })
        return true
    }

    override fun onResume() {
        super.onResume()
        getData(null)
    }

    private fun getData(query: String?) {
        progress_bar.visibility = View.VISIBLE
        list_kelas.layoutManager = LinearLayoutManager(this)
        list_kelas.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null) {

        } else {

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_button -> {
                val intent = Intent(this, AddKelasActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}