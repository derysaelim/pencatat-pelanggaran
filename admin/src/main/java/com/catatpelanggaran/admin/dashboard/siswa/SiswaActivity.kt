package com.catatpelanggaran.admin.dashboard.siswa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.catatpelanggaran.admin.R
import kotlinx.android.synthetic.main.activity_siswa.*

class SiswaActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siswa)
        setSupportActionBar(toolbar_siswa)

        back_button.setOnClickListener(this)
        add_button.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.back_button -> {
                finish()
            }
            R.id.add_button -> {
                val intent = Intent(this, AddSiswaActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}