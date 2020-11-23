package com.catatpelanggaran.admin.dashboard.siswa

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.adapter.AdapterSiswa
import com.catatpelanggaran.admin.dashboard.guru.AddGuruActivity
import com.catatpelanggaran.admin.model.Guru
import com.catatpelanggaran.admin.model.Siswa
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_siswa.*

class SiswaActivity : AppCompatActivity() {

    var listSiswa: ArrayList<Siswa>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siswa)
        setSupportActionBar(toolbar_siswa)

        back_button.setOnClickListener {
            finish()
        }

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
                getData(query)
                return true
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_button -> {
                val intent = Intent(this, AddSiswaActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun getData(query: String?) {
        list_siswa.layoutManager = LinearLayoutManager(this)
        list_siswa.hasFixedSize()
        progress_bar.visibility = View.VISIBLE
        val database = FirebaseDatabase.getInstance().reference
        if (query != null) {
            listSiswa = arrayListOf<Siswa>()
            val search = query.replace("\\s".toRegex(), "")
            database.child("Siswa").orderByChild("nama_siswa").startAt(search)
                .endAt(search + "\uf8ff").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listSiswa!!.clear()
                            for (i in snapshot.children) {
                                val siswa = i.getValue(Siswa::class.java)
                                listSiswa?.add(siswa!!)
                            }

                            val adapter = AdapterSiswa(listSiswa!!)
                            list_siswa.adapter = adapter
                            progress_bar.visibility = View.GONE
                            list_siswa.visibility = View.VISIBLE
                            siswa_empty.visibility = View.GONE

                        } else {
                            list_siswa.visibility = View.GONE
                            siswa_empty.visibility = View.VISIBLE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        siswa_empty.visibility = View.VISIBLE
                    }

                })
        } else {
            listSiswa = arrayListOf<Siswa>()
            database.child("Siswa").orderByChild("nama_siswa")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listSiswa!!.clear()
                            for (i in snapshot.children) {
                                val siswa = i.getValue(Siswa::class.java)
                                listSiswa?.add(siswa!!)
                            }
                            val adapter = AdapterSiswa(listSiswa!!)
                            list_siswa.adapter = adapter
                            progress_bar.visibility = View.GONE
                            list_siswa.visibility = View.VISIBLE
                            siswa_empty.visibility = View.GONE

                        } else {
                            list_siswa.visibility = View.GONE
                            siswa_empty.visibility = View.VISIBLE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        siswa_empty.visibility = View.VISIBLE
                    }

                })
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}