package com.catatpelanggaran.admin.dashboard.pelanggaran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Guru
import com.catatpelanggaran.admin.model.Pelanggaran
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_pelanggaran.*
import java.lang.Exception

class AddPelanggaranActivity : AppCompatActivity() {

    companion object {
        const val DATA_PELANGGARAN = "DATAPELANGGARAN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pelanggaran)

        val dataPelanggaran = intent.getParcelableExtra<Pelanggaran>(DATA_PELANGGARAN)

        if (dataPelanggaran != null) {
            setText(dataPelanggaran)
            setStatus(true)
        } else {
            setStatus(false)
        }

        button_simpan.setOnClickListener {
            if (dataPelanggaran != null) {
                editData(dataPelanggaran)
            } else {
                insertData()
            }
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_button.setOnClickListener {
            deleteData(dataPelanggaran)
        }

    }

    private fun setText(dataPelanggaran: Pelanggaran?) {
        TODO("Not yet implemented")
    }

    private fun setStatus(status: Boolean) {

    }

    private fun deleteData(dataPelanggaran: Pelanggaran?) {
        TODO("Not yet implemented")
    }

    private fun insertData() {
        val jenis = input_pelanggaran.text.toString()
        val poin = input_poin.text.toString()

        if (jenis.isEmpty() || poin.isEmpty()) {
            if (jenis.isEmpty()) {
                input_pelanggaran.error = "Tolong isi"
            }
            if (poin.isEmpty()) {
                input_poin.error = "Tolong isi"
            }
        } else {
            val database = FirebaseDatabase.getInstance().reference
            val pelanggaranId = database.push().key
            database.child("jenis_pelanggaran").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(pelanggaranId.toString()).child(jenis).exists()) {
                        Toast.makeText(this@AddPelanggaranActivity, "Sudah ada", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        try {
                            val data = Pelanggaran(pelanggaranId, jenis, poin)
                            database.child("jenis_pelanggaran").child(pelanggaranId.toString())
                                .setValue(data).addOnCompleteListener {
                                    Toast.makeText(
                                        this@AddPelanggaranActivity,
                                        "berhasil",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@AddPelanggaranActivity,
                                "check your internet",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AddPelanggaranActivity,
                        "Something wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    private fun editData(dataPelanggaran: Pelanggaran) {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}