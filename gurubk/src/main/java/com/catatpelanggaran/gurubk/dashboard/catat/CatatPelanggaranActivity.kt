package com.catatpelanggaran.gurubk.dashboard.catat

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Catat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_catat_pelanggaran.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_siswa.*
import java.util.*
import kotlin.collections.ArrayList

class CatatPelanggaranActivity : AppCompatActivity() {

    companion object {
        const val DATA_PELANGGAR = "dataPelanggar"
    }

    lateinit var adapterPelanggaran: ArrayAdapter<String>
    lateinit var dataListPelanggaran: ArrayList<String>
    lateinit var dataListHukuman: ArrayList<String>
    lateinit var dataListPoin: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catat_pelanggaran)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        input_tanggal.setOnClickListener {
            val dpd = DatePickerDialog(this, { _, mYear, mMonth, mDay ->
                val mmMonth = mMonth + 1
                input_tanggal.setText("" + mDay + "/" + mmMonth + "/" + mYear)
            }, year, month, day)
            dpd.show()
        }

        dataListHukuman = ArrayList()
        dataListPoin = ArrayList()
        dataListPelanggaran = ArrayList()
        adapterPelanggaran = ArrayAdapter(
            this@CatatPelanggaranActivity,
            android.R.layout.simple_spinner_dropdown_item,
            dataListPelanggaran
        )
        jenispel.adapter = adapterPelanggaran

        jenispel.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val hukuman = dataListHukuman[jenispel.selectedItemId.toInt()]
                text_hukuman.setText(hukuman)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        var dataCatat = intent.getParcelableExtra<Catat>(DATA_PELANGGAR)

        val database = FirebaseDatabase.getInstance().reference
        database.child("Pelanggar").child(dataCatat?.nis!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val nama = snapshot.child("nama_siswa").value.toString()
                        val tanggal = snapshot.child("tanggal").value.toString()
                        val nis = snapshot.child("nis").value.toString()
                        val poin = snapshot.child("poinPelanggaran").value.toString()
                        val namaPelanggaran = snapshot.child("namaPelanggaran").value.toString()
                        val keterangan = snapshot.child("keterangan").value.toString()
                        val hukuman = snapshot.child("hukuman").value.toString()

                        dataCatat = Catat(
                            nis,
                            nama,
                            tanggal,
                            namaPelanggaran,
                            keterangan,
                            hukuman,
                            poin.toInt()
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })

        if (dataCatat != null) {
            if (dataCatat!!.tanggal != null) {
                setStatus(false)
                setJenpel(true)
                getData(dataCatat)
            } else {
                setStatus(true)
                setJenpel(false)
                getData(dataCatat)
                retrieveData()
            }
        }
        else {
            retrieveData()
        }

        button_simpanpelanggaran.setOnClickListener {
            if (dataCatat?.tanggal != null) {
                editData(dataCatat!!)
            } else {
                insertData(dataCatat!!)
            }
        }

        back_button.setOnClickListener {
            onBackPressed()
        }
    }

    private fun insertData(dataCatat: Catat) {
        val database = FirebaseDatabase.getInstance().reference
        val tanggal = input_tanggal.text.toString()
        val nis = dataCatat.nis.toString()
        val namaSiswa = dataCatat.nama_siswa.toString()
        val idpelanggaran = jenispel.selectedItemId
        val jenispel = jenispel.selectedItem.toString()
        val keterangan = input_keterangan.text.toString()
        val poin = dataListPoin[idpelanggaran.toInt()].toInt()
        val hukuman = dataListHukuman[idpelanggaran.toInt()]

        val data = Catat(nis, namaSiswa, tanggal, jenispel, keterangan, hukuman, poin)

        if (tanggal.isEmpty() || nis.isEmpty() || namaSiswa.isEmpty() || keterangan.isEmpty()) {
            if (tanggal.isEmpty()) {
                input_tanggal.error = "Data tidak boleh kosong!"
            }
            if (nis.isEmpty()) {
                detail_nis.error = "Data tidak boleh kosong!"
            }
            if (namaSiswa.isEmpty()) {
                detail_nama.error = "Data tidak boleh kosong!"
            }
            if (keterangan.isEmpty()) {
                input_keterangan.error = "Data tidak boleh kosong!"
            }

        }
        else {
            database.child("Pelanggar").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
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
        val idpelanggaran = jenispel.selectedItemId
        val jenispel = jenispel.selectedItem.toString()
        val keterangan = input_keterangan.text.toString()
        val poin = dataListPoin[idpelanggaran.toInt()].toInt()
        val hukuman = dataListHukuman[idpelanggaran.toInt()]

        val update = Catat(
            nis, namaSiswa, "${dataCatat.tanggal + ", " + tanggal}",
            "${dataCatat.namaPelanggaran + ", " + jenispel}",
            "${dataCatat.keterangan + ", " + keterangan}",
            "${dataCatat.hukuman + ", " + hukuman}",
            dataCatat.poinPelanggaran + poin
        )
        if (tanggal.isEmpty() || nis.isEmpty() || namaSiswa.isEmpty() || keterangan.isEmpty()) {

        } else {
            try {
                database.child("Pelanggar").child(nis).setValue(update).addOnSuccessListener {
                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Check your internet", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Check your internet", Toast.LENGTH_SHORT).show()
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
            detail_pelanggaran.visibility = View.GONE
        }
    }

    private fun setJenpel(status: Boolean) {
        if (status) {
            message_button.visibility = View.VISIBLE
            jenispel.visibility = View.GONE
        }
    }

    private fun retrieveData() {
        val database = FirebaseDatabase.getInstance().reference
        database.child("jenis_pelanggaran").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    dataListPelanggaran.add(i.child("namaPelanggaran").value.toString())
                    dataListPoin.add(i.child("poinPelanggaran").value.toString())
                    dataListHukuman.add(i.child("hukuman").value.toString())
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