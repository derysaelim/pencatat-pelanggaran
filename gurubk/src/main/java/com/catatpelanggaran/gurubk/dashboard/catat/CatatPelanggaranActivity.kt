package com.catatpelanggaran.gurubk.dashboard.catat

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
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
                text_hukuman.setText(jenispel.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val dataCatat = intent.getParcelableExtra<Catat>(DATA_PELANGGAR)
        if (dataCatat != null) {
            if (dataCatat.tanggal != null) {
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

    private fun insertData(dataCatat: Catat) {
        val database = FirebaseDatabase.getInstance().reference
        val tanggal = input_tanggal.text.toString()
        val nis = dataCatat.nis.toString()
        val namaSiswa = dataCatat.nama_siswa.toString()
        val telp_ortu = dataCatat.telp_ortu.toString()
        val idpelanggaran = jenispel.selectedItemId
        val jenispel = jenispel.selectedItem.toString().replace("\\s".toRegex(), "")
        val keterangan = input_keterangan.text.toString()

        val poin = dataListPoin[idpelanggaran.toInt()].toInt()
        val hukuman = dataListHukuman[idpelanggaran.toInt()]

        val data = Catat(tanggal, nis, namaSiswa, telp_ortu, jenispel, keterangan, poin, hukuman)

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

        val tanggal = dataCatat.tanggal.toString()
        val nis = dataCatat.nis.toString()
        val namaSiswa = dataCatat.nama_siswa.toString()
        val telp_ortu = dataCatat.telp_ortu.toString()
        val jenispel = jenispel.selectedItem.toString().replace("\\s".toRegex(), "")
        val keterangan = input_keterangan.text.toString()

        if (tanggal.isEmpty() || nis.isEmpty() || namaSiswa.isEmpty() || keterangan.isEmpty()) {

        }
        else {
            try {
                val database = FirebaseDatabase.getInstance().reference.child("Pelanggar")
                val update = Catat(tanggal, dataCatat.nis, namaSiswa, telp_ortu, jenispel, keterangan)
                database.child(nis).setValue(update).addOnSuccessListener {
                    Toast.makeText(this, "Berhasil Update", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Check your internet", Toast.LENGTH_SHORT).show()
                }
            }
            catch (e: Exception) {
                Toast.makeText(this, "Check your internet", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getData(dataCatat: Catat?) {
        dataCatat?.let {

            input_tanggal.setText(dataCatat.tanggal)
            detail_nis.setText(dataCatat.nis.toString())
            detail_nama.setText(dataCatat.nama_siswa)
            detail_telepon.setText(dataCatat.telp_ortu)
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