package com.catatpelanggaran.admin.dashboard.siswa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.catatpelanggaran.admin.R
import kotlinx.android.synthetic.main.activity_add_siswa.*

class AddSiswaActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_siswa)

        back_button.setOnClickListener(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.back_button -> {
                finish()
            }
        }
    }
}