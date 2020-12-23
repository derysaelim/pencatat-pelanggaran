package com.catatpelanggaran.gurubk.dashboard.catat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isGone
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Catat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_catat_pelanggaran.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_siswa.*
import kotlin.collections.ArrayList

class CatatPelanggaranActivity : AppCompatActivity() {

    companion object {
        const val DATA_PELANGGAR = "dataPelanggar"
    }

    lateinit var adapterPelanggaran: ArrayAdapter<String>
    lateinit var dataListPelanggaran: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catat_pelanggaran)

        dataListPelanggaran = ArrayList()
        adapterPelanggaran = ArrayAdapter(
            this@CatatPelanggaranActivity, android.R.layout.simple_spinner_dropdown_item, dataListPelanggaran
        )
        jenispel.adapter = adapterPelanggaran

        val dataCatat = intent.getParcelableExtra<Catat>(DATA_PELANGGAR)
        if (dataCatat != null) {
            if (dataCatat.tanggal != null) {
                setStatus(false)
                setJenpel(true)
                getData(dataCatat)
                retrieveData(true, dataCatat)
            }
            else {
                setStatus(true)
                setJenpel(false)
                getData(dataCatat)
                retrieveData(true, dataCatat)
            }
        }
        else {
            retrieveData(false, null)
        }

        button_simpanpelanggaran.setOnClickListener {
            if (dataCatat != null) {
                if (dataCatat.tanggal != null) {
                    editData(dataCatat)
                } else {
                    insertData(dataCatat)
                }
            }
        }

        back_button.setOnClickListener {
            onBackPressed()
        }
    }

    private fun insertData(dataCatat: Catat){
        val database = FirebaseDatabase.getInstance().reference

        val tanggal = input_tanggal.text.toString()
        val nis = dataCatat.nis.toString()
        val namaSiswa = dataCatat.nama_siswa.toString()
        val jenispel = jenispel.selectedItem.toString().replace("\\s".toRegex(), "")
        val keterangan = input_keterangan.text.toString()

        val data = Catat(tanggal, nis, namaSiswa, jenispel, keterangan)

        if (tanggal.isEmpty() || nis.isEmpty() || namaSiswa.isEmpty() || keterangan.isEmpty()) {

        }
        else {
            database.child("Pelanggar").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(nis).exists()) {
                    }
                    else {
                        try {
                            database.child("Pelanggar").child(nis).setValue(data).addOnCompleteListener {
                                Toast.makeText(this@CatatPelanggaranActivity, "Berhasil!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                        catch (e: Exception) {
                            Toast.makeText(this@CatatPelanggaranActivity, "Check your internet", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun editData(dataCatat: Catat) {
        val database = FirebaseDatabase.getInstance().reference

        val tanggal = input_tanggal.text.toString()
        val nis = dataCatat.nis.toString()
        val namaSiswa = dataCatat.nama_siswa.toString()
        val jenispel = jenispel.selectedItem.toString().replace("\\s".toRegex(), "")
        val keterangan = input_keterangan.text.toString()

        val data = Catat(tanggal, nis, namaSiswa, jenispel, keterangan)

        if (tanggal.isEmpty() || nis.isEmpty() || namaSiswa.isEmpty() || keterangan.isEmpty()) {

        }
        else {
            if (jenispel != dataCatat.namaPelanggaran) {
                database.child("Pelanggar").orderByChild("namaPelaanggaran").equalTo(dataCatat.namaPelanggaran)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            database.child("Pelanggar").child(dataCatat.namaPelanggaran!!).removeValue()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

            }
        }

    }

    private fun getData(dataCatat: Catat?) {
        dataCatat?.let {

            input_tanggal.setText(dataCatat.tanggal)
            detail_nis.setText(dataCatat.nis.toString())
            detail_nama.setText(dataCatat.nama_siswa)
            detail_pelanggaran.setText(dataCatat.namaPelanggaran)
            input_keterangan.setText(dataCatat.keterangan)
        }
    }

    private fun setStatus(status: Boolean) {
        if (status) {
            tv_pelanggaran.visibility = View.GONE
            detail_pelanggaran.visibility = View.GONE
        }
    }

    private fun setJenpel(status: Boolean) {
        if (status) {
            message_button.visibility = View.VISIBLE
            tv_jenpel.visibility = View.GONE
            jenispel.visibility = View.GONE
        }
    }

    private fun retrieveData(status: Boolean, dataCatat: Catat?) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("jenis_pelanggaran").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    dataListPelanggaran.add(i.child("namaPelanggaran").value.toString())
                }
                adapterPelanggaran.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CatatPelanggaranActivity, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}