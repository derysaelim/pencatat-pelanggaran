package com.catatpelanggaran.orangtua.dashboard.penghargaan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.catatpelanggaran.orangtua.R

class PenghargaanActivity : AppCompatActivity() {

    companion object {
        const val NIS_SISWA = "NIS_SISWA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penghargaan)
    }
}