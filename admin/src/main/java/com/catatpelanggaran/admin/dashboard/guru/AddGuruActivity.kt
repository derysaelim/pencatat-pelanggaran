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

class AddGuruActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_guru)

        button_simpan.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_simpan -> {
                insertData()
            }
        }
    }

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
                        Toast.makeText(
                            this@AddGuruActivity,
                            "Sudah ada",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val data = Guru(nip, name, nohp)
                        database.child("Guru").child(nip).setValue(data)
                            .addOnCompleteListener {
                                Toast.makeText(
                                    this@AddGuruActivity,
                                    "berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }.addOnFailureListener {

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
    }
}