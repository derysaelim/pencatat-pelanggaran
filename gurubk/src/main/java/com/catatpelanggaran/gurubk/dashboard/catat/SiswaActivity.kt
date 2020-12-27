package com.catatpelanggaran.gurubk.dashboard.catat

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.adapter.AdapterSiswa
import com.catatpelanggaran.gurubk.model.Catat
import com.catatpelanggaran.gurubk.model.Siswa
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_catat_pelanggaran.back_button
import kotlinx.android.synthetic.main.activity_siswa.*

class SiswaActivity : AppCompatActivity() {

    companion object {
        const val DATA_SISWA = "dataSiswa"
    }

    var listSiswa: ArrayList<Catat>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siswa)
        setSupportActionBar(toolbar_siswa)

        back_button.setOnClickListener {
            onBackPressed()
        }

        getData(null)
    }

    override fun onResume() {
        super.onResume()
        getData(null)
    }

    private fun getData(query: String?) {
        progress_bar.visibility = View.VISIBLE
        list_siswa.layoutManager = LinearLayoutManager(this)
        list_siswa.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null){
            val search = query.replace("\\s".toRegex(), "")
            listSiswa = arrayListOf<Catat>()
            database.child("Siswa").orderByChild("nama_siswa").startAt(search).endAt(search + "\uf8ff")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@SiswaActivity, "Somethings wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listSiswa!!.clear()
                            for (i in snapshot.children) {
                                val siswa = i.getValue(Catat::class.java)
                                listSiswa!!.add(siswa!!)
                            }
                            val adapter = AdapterSiswa(listSiswa!!)
                            list_siswa.adapter = adapter
                            progress_bar.visibility = View.GONE
                            siswa_empty.visibility = View.GONE
                            list_siswa.visibility = View.VISIBLE

                            adapter.onItemClick = { selectedSiswa ->
                                val intent =
                                    Intent(this@SiswaActivity, CatatPelanggaranActivity::class.java)
                                intent.putExtra(
                                    CatatPelanggaranActivity.DATA_PELANGGAR,
                                    selectedSiswa
                                )
                                startActivity(intent)
                            }
                        }
                        else {
                            siswa_empty.visibility = View.VISIBLE
                            list_siswa.visibility = View.GONE
                        }
                    }
                })
        }
        else {
            listSiswa = arrayListOf<Catat>()
            database.child("Siswa").orderByChild("nama_siswa")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@SiswaActivity, "Somethings wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listSiswa!!.clear()
                            for (i in snapshot.children) {
                                val siswa = i.getValue(Catat::class.java)
                                listSiswa!!.add(siswa!!)
                            }
                            val adapter = AdapterSiswa(listSiswa!!)
                            list_siswa.adapter = adapter
                            progress_bar.visibility = View.GONE
                            siswa_empty.visibility = View.GONE
                            list_siswa.visibility = View.VISIBLE

                            adapter.onItemClick = { selectedSiswa ->
                                val intent = Intent(this@SiswaActivity, CatatPelanggaranActivity::class.java)
                                intent.putExtra(
                                    CatatPelanggaranActivity.DATA_PELANGGAR,
                                    selectedSiswa
                                )
                                startActivity(intent)

                            }
                        }
                        else {
                            siswa_empty.visibility = View.VISIBLE
                            list_siswa.visibility = View.GONE
                        }
                    }
                })

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search_bar).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Cari"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                getData(query)
                return true
            }
        })
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}