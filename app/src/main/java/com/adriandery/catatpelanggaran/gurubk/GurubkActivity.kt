package com.adriandery.catatpelanggaran.gurubk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adriandery.catatpelanggaran.LoginActivity
import com.adriandery.catatpelanggaran.R
import com.adriandery.catatpelanggaran.SharedPreferences
import kotlinx.android.synthetic.main.activity_gurubk.*

class GurubkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurubk)

        logout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            SharedPreferences.clearData(this)
            finish()
        }
    }
}