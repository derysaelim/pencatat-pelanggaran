package com.catatpelanggaran.admin.dashboard.guru

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Guru
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_guru.*
import java.lang.Exception

class AddGuruActivity : AppCompatActivity() {

    companion object {
        const val DATA_GURU = "DATAGURU"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_guru)

        val dataGuru = intent.getParcelableExtra<Guru>(DATA_GURU)
        if (dataGuru != null) {
            setText(dataGuru)
            setStatus(true)
        } else {
            setStatus(false)
        }

        button_simpan.setOnClickListener {
            if (dataGuru != null) {
                editData(dataGuru)
            } else {
                insertData()
            }
        }
    }

    private fun setStatus(status: Boolean) {
        if (status) {
            input_nip.isEnabled = false
            button_simpan.text = "Edit"
        } else {
            button_simpan.text = "Simpan"
        }
    }

    private fun setText(dataGuru: Guru?) {
        dataGuru?.let {
            input_nip.setText(dataGuru.nip)
            input_nama.setText(dataGuru.nama)
            input_nohp.setText(dataGuru.nohp)
        }
    }

    private fun editData(dataGuru: Guru) {
        val nip = dataGuru.nip.toString()
        val name = input_nama.text.toString()
        val nohp = input_nohp.text.toString()

        if (name.isEmpty() || nohp.isEmpty()) {
            if (name.isEmpty()) {
                input_nama.error = "Isi Flish"
            }
            if (nohp.isEmpty()) {
                input_nohp.error = "Isi Flish"
            }

        } else {
            try {
                val database = FirebaseDatabase.getInstance().reference.child("Guru")
                val update = Guru(dataGuru.nip, name, nohp)
                database.child(nip).setValue(update).addOnSuccessListener {
                    Toast.makeText(this, "berhasil update", Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {
                        Toast.makeText(this, "gagal update", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(this, "check your internet", Toast.LENGTH_SHORT).show()
            }

        }

    }
//
//    private fun deleteData() {
//        val database = FirebaseDatabase.getInstance().reference.child("Guru").child()
//    }

    private fun insertData() {
        val nip = input_nip.text.toString()
        val name = input_nama.text.toString()
        val nohp = input_nohp.text.toString()

        if (nip.isEmpty() || name.isEmpty() || nohp.isEmpty()) {

            if (nip.isEmpty()) {
                input_nip.error = "Isi Flish"
            }
            if (name.isEmpty()) {
                input_nama.error = "Isi Flish"
            }
            if (nohp.isEmpty()) {
                input_nohp.error = "Isi Flish"
            }

        } else {
            val database = FirebaseDatabase.getInstance().reference

            database.child("Guru").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(nip).exists()) {
                        Toast.makeText(this@AddGuruActivity, "Sudah ada", Toast.LENGTH_SHORT).show()
                    } else {

                        try {
                            val data = Guru(nip, name, nohp)
                            database.child("Guru").child(nip).setValue(data).addOnCompleteListener {
                                Toast.makeText(this@AddGuruActivity, "berhasil", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@AddGuruActivity,
                                "check your internet",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AddGuruActivity,
                        "Something wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}