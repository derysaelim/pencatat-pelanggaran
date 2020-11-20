package com.catatpelanggaran.admin.dashboard.kelas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Kelas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_guru.back_button
import kotlinx.android.synthetic.main.activity_add_guru.button_simpan
import kotlinx.android.synthetic.main.activity_add_guru.delete_button
import kotlinx.android.synthetic.main.activity_add_kelas.*
import kotlinx.android.synthetic.main.activity_add_kelas.input_jurusan
import kotlinx.android.synthetic.main.activity_add_kelas.input_tingkat
import kotlinx.android.synthetic.main.activity_add_kelas.kelas_B
import kotlinx.android.synthetic.main.activity_add_kelas.kelas_a
import kotlinx.android.synthetic.main.activity_add_kelas.kelas_c
import kotlinx.android.synthetic.main.activity_add_kelas.kelas_d
import kotlinx.android.synthetic.main.activity_add_siswa.*
import kotlinx.android.synthetic.main.item_siswa.*


class AddKelasActivity : AppCompatActivity() {

    companion object {
        const val DATA_KELAS = "DATAKELAS"
    }

    lateinit var adapter: ArrayAdapter<String>
    lateinit var spinnerDataList: ArrayList<String>
    lateinit var dataList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_kelas)
        dataList = ArrayList()
        spinnerDataList = ArrayList()
        adapter = ArrayAdapter(
            this@AddKelasActivity,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerDataList
        )
        walikelas.adapter = adapter

        val dataKelas = intent.getParcelableExtra<Kelas>(DATA_KELAS)
        if (dataKelas != null) {
            setText(dataKelas)
            setStatus(true)
        } else {
            setStatus(false)
        }

        button_simpan.setOnClickListener {
            if (dataKelas != null) {
                editData(dataKelas)
            } else {
                insertData()
            }
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_button.setOnClickListener {
            deleteData(dataKelas)
        }
        retrieveData(dataKelas)
    }

    private fun retrieveData(dataKelas: Kelas?) {

        val database = FirebaseDatabase.getInstance().reference

        if (dataKelas != null) {
            database.child("Guru").orderByChild("nama").startAt(dataKelas.nip).endAt(dataKelas.nip)
                .addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (i in snapshot.children) {
                            spinnerDataList.add(i.child("nama").value.toString())
                            dataList.add(i.child("nip").value.toString())
                        }
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@AddKelasActivity, "something wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
        } else {
            database.child("Guru").orderByChild("nama").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        spinnerDataList.add(i.child("nama").value.toString())
                        dataList.add(i.child("nip").value.toString())
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AddKelasActivity, "something wrong", Toast.LENGTH_SHORT)
                        .show()
                }

            })
        }
    }

    private fun deleteData(dataKelas: Kelas?) {
    }

    private fun insertData() {
        val tingkat = input_tingkat.selectedItem.toString()
        val jurusan = input_jurusan.text.toString().trim()
        val walikelas = walikelas.selectedItemId
        var kelas = ""
        if (kelas_a.isChecked) {
            kelas = "A"
        }
        if (kelas_B.isChecked) {
            kelas = "B"
        }
        if (kelas_c.isChecked) {
            kelas = "C"
        }
        if (kelas_d.isChecked) {
            kelas = "D"
        }
        val idKelas = tingkat + jurusan + kelas

        if (jurusan.isEmpty() || kelas.isEmpty()) {
            if (jurusan.isEmpty()) {
                input_jurusan.error = "Isi"
            }
            if (kelas.isEmpty()) {
                no_kelas.visibility = View.VISIBLE
            }
        }

        val nip = dataList[walikelas.toInt()]

        val database = FirebaseDatabase.getInstance().reference
        database.child("kelas").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(idKelas).exists()) {
                    Toast.makeText(this@AddKelasActivity, "Sudah ada", Toast.LENGTH_SHORT).show()
                } else {
                    database.child("walikelas")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.child(nip).exists()) {
                                    Toast.makeText(
                                        this@AddKelasActivity,
                                        "Sudah ada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val data = Kelas(idKelas, tingkat, jurusan, kelas, nip)
                                    database.child("walikelas").child(nip).setValue(true)
                                        .addOnCompleteListener {
                                            database.child("kelas").child(idKelas).setValue(data)
                                                .addOnCompleteListener {
                                                    Toast.makeText(
                                                        this@AddKelasActivity,
                                                        "Berhasil",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    finish()
                                                }
                                        }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    this@AddKelasActivity,
                                    "something wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddKelasActivity, "something wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editData(dataKelas: Kelas) {
        TODO("Not yet implemented")
    }

    private fun setStatus(status: Boolean) {
        if (status) {

        } else {

        }
    }

    private fun setText(dataKelas: Kelas?) {
        dataKelas?.let {

            if (dataKelas.tingkat == "X") {
                input_tingkat.setSelection(0)
            } else if (dataKelas.tingkat == "B") {
                input_tingkat.setSelection(1)
            } else if (dataKelas.tingkat == "C") {
                input_tingkat.setSelection(2)
            } else {
                input_tingkat.setSelection(3)
            }

            input_jurusan.setText(dataKelas.jurusan)

            when (dataKelas.kelas) {
                "A" -> {
                    kelas_a.isChecked = true
                }
                "B" -> {
                    kelas_B.isChecked = true
                }
                "C" -> {
                    kelas_c.isChecked = true
                }
                else -> {
                    kelas_d.isChecked = true
                }
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}