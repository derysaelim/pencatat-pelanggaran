package com.catatpelanggaran.admin.dashboard.guru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.adapter.AdapterGuru
import com.catatpelanggaran.admin.model.Guru
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_guru.*

class GuruActivity : AppCompatActivity(), View.OnClickListener {

    var listGuru: ArrayList<Guru>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guru)
        setSupportActionBar(toolbar_guru)

        back_button.setOnClickListener(this)
        add_button.setOnClickListener(this)
        getData()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.back_button -> {
                finish()
            }
            R.id.add_button -> {
                val intent = Intent(this, AddGuruActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        progress_bar.visibility = View.VISIBLE
        list_guru.layoutManager = LinearLayoutManager(this)
        list_guru.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference
        listGuru = arrayListOf<Guru>()
        database.child("Guru").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listGuru!!.clear()
                    for (i in snapshot.children) {
                        val guru = i.getValue(Guru::class.java)
                        listGuru?.add(guru!!)
                    }
                    val adapter = AdapterGuru(listGuru!!)
                    list_guru.adapter = adapter
                    progress_bar.visibility = View.GONE
                    guru_empty.visibility = View.GONE

                    adapter.onItemClick = { selectedGuru ->
                        val intent = Intent(this@GuruActivity, AddGuruActivity::class.java)
                        intent.putExtra(AddGuruActivity.DATA_GURU, selectedGuru)
                        startActivity(intent)
                    }
                } else {
                    guru_empty.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progress_bar.visibility = View.GONE
                Toast.makeText(this@GuruActivity, "Somethings wrong", Toast.LENGTH_SHORT).show()
            }

        })
    }
}