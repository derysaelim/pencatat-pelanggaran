package com.adriandery.catatpelanggaran

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adriandery.catatpelanggaran.admin.AdminActivity
import com.adriandery.catatpelanggaran.gurubk.GurubkActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {
//            Toast.makeText(this, "$nip and $password", Toast.LENGTH_SHORT).show()

            val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
            databaseReference.child("Login").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nip = nip_login.text.toString()
                    val password = password_login.text.toString()
//                        cek NIP didatabase
                    if (snapshot.child(nip).exists()) {
//                        cek password didatabase
                        if (snapshot.child(nip).child("password").getValue(String::class.java)
                                .equals(password)
                        ) {
//                            cek apakah admin
                            if (snapshot.child(nip).child("role").getValue(String::class.java)
                                    .equals("admin")
                            ) {

//                                set user sudah login
                                SharedPreferences.setDataLogin(this@LoginActivity, true)

//                                set login sebagai admin
                                SharedPreferences.setDataAs(this@LoginActivity, "admin")
                                val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {

//                                set user sudah login
                                SharedPreferences.setDataLogin(this@LoginActivity, true)

//                                set login sebagai guru bk
                                SharedPreferences.setDataAs(this@LoginActivity, "gurubk")
                                val intent = Intent(this@LoginActivity, GurubkActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Password salah", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Belum terdaftar", Toast.LENGTH_LONG)
                            .show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Belum terdaftar", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    override fun onStart() {
        super.onStart()

//        akan dieksekusi apabila user sudah login dan tidak melakukan logout

        if (SharedPreferences.getDataLogin(this)) {
            if (SharedPreferences.getDataAs(this).equals("admin")) {
                val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@LoginActivity, GurubkActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}