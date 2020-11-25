package com.catatpelanggaran.gurubk.dashboard.catat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.catatpelanggaran.gurubk.R
import kotlinx.android.synthetic.main.activity_catat_pelanggaran.*

class SiswaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siswa)

        back_button.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}