package com.catatpelanggaran.gurubk.dashboard.gurubk

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.adapter.AdapterGuru
import com.catatpelanggaran.gurubk.model.Guru
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_bk.*

class BkActivity : AppCompatActivity() {

    companion object {
        const val NIP_GURUBK = "nipgurubk"
    }

    var listGuruBK: ArrayList<Guru>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView
    lateinit var nip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bk)
        setSupportActionBar(toolbar_gurubk)

        back_button.setOnClickListener { finish() }

        nip = intent.getStringExtra(NIP_GURUBK).toString()
        Log.e("NIP", nip)

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
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                getData(query)
                return true
            }

        })
        return true
    }

    private fun getData(query: String?) {
        progress_bar.visibility = View.VISIBLE
        list_gurubk.layoutManager = LinearLayoutManager(this)
        list_gurubk.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null) {
            listGuruBK = arrayListOf<Guru>()
            database.child("Guru_BK").orderByChild("nama").startAt(query).endAt(query + "\uf8ff")
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                listGuruBK!!.clear()
                                for (i in snapshot.children) {
                                    val gurubk = i.getValue(Guru::class.java)
                                    listGuruBK!!.add(gurubk!!)
                                }
                                val adapter = AdapterGuru(listGuruBK!!)
                                list_gurubk.adapter = adapter
                                progress_bar.visibility = View.GONE
                                list_gurubk.visibility = View.VISIBLE
                                gurubk_empty.visibility = View.GONE

                            } else {
                                gurubk_empty.visibility = View.VISIBLE
                                list_gurubk.visibility = View.GONE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@BkActivity, "something wrong", Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
        } else {
            listGuruBK = arrayListOf<Guru>()
            database.child("Guru_BK").orderByChild("nama")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listGuruBK!!.clear()
                            for (i in snapshot.children) {
                                val gurubk = i.getValue(Guru::class.java)
                                listGuruBK!!.add(gurubk!!)
                            }
                            val adapter = AdapterGuru(listGuruBK!!)
                            list_gurubk.adapter = adapter
                            progress_bar.visibility = View.GONE
                            list_gurubk.visibility = View.VISIBLE
                            gurubk_empty.visibility = View.GONE

                        } else {
                            gurubk_empty.visibility = View.VISIBLE
                            list_gurubk.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@BkActivity, "something wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
        }

    }


    override fun onResume() {
        super.onResume()
        getData(null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}