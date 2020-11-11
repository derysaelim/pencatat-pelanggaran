package com.adriandery.catatpelanggaran

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {

            val nip = nip_login.text.toString()
            val password = password_login.text.toString()

            if (nip.isEmpty() || password.isEmpty()) {
                if (nip.isEmpty()) {
                    nip_login.error = "Mohon isi"
                }

                if (password.isEmpty()) {
                    password_login.error = "Mohon isi"
                }
            } else {

                if (isNetworkAvailable(this)) {
                    val databaseReference: DatabaseReference =
                        FirebaseDatabase.getInstance().reference
                    databaseReference.child("Login")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
//                        cek NIP didatabase
                                Toast.makeText(
                                    this@LoginActivity,
                                    "mau check nip",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                if (snapshot.child(nip).exists()) {
//                        cek password didatabase
                                    if (snapshot.child(nip).child("password")
                                            .getValue(String::class.java)
                                            .equals(password)
                                    ) {
//                            cek apakah admin
                                        if (snapshot.child(nip).child("role")
                                                .getValue(String::class.java)
                                                .equals("admin")
                                        ) {

//                                set user sudah login
                                            SharedPreferences.setDataLogin(this@LoginActivity, true)

//                                set login sebagai admin
                                            SharedPreferences.setDataAs(this@LoginActivity, "admin")
                                            goToModule("admin")
                                            finish()
                                        } else {

//                                set user sudah login
                                            SharedPreferences.setDataLogin(this@LoginActivity, true)

//                                set login sebagai guru bk
                                            SharedPreferences.setDataAs(
                                                this@LoginActivity,
                                                "gurubk"
                                            )
                                            goToModule("gurubk")
                                            finish()
                                            finish()
                                        }
                                    } else {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Password salah",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Belum terdaftar",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Belum terdaftar",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }

                        })
                } else {
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()

//        akan dieksekusi apabila user sudah login dan tidak melakukan logout

        if (SharedPreferences.getDataLogin(this)) {
            if (SharedPreferences.getDataAs(this).equals("admin")) {
                goToModule("admin")
                finish()
            } else {
                goToModule("gurubk")
                finish()
            }
        }
    }

    private fun goToModule(moduleName: String) {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        if (splitInstallManager.installedModules.contains(moduleName)) {

            if (moduleName == "admin") {
                startActivity(
                    Intent(
                        this,
                        Class.forName("com.catatpelanggaran.admin.AdminActivity")
                    )
                )
            } else {
                startActivity(
                    Intent(
                        this,
                        Class.forName("com.catatpelanggaran.gurubk.GurubkActivity")
                    )
                )
            }
        }

    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }

}