package com.catatpelanggaran.admin.dashboard.kelas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Kelas
import kotlinx.android.synthetic.main.activity_add_guru.*

class AddKelasActivity : AppCompatActivity() {

    companion object {
        const val DATA_KELAS = "DATAKELAS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_kelas)

        val dataKelas = intent.getParcelableExtra<Kelas>(DATA_KELAS)

        if (dataKelas != null) {
            setText(dataKelas)
            setStatus(true)
        } else {
            setStatus(false)
        }

        button_simpan.setOnClickListener {
            if (dataKelas != null) {
                editData(dataKelas)
            } else {
                insertData()
            }
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_button.setOnClickListener {
            deleteData(dataKelas)
        }

    }

    private fun deleteData(dataKelas: Kelas?) {
        TODO("Not yet implemented")
    }

    private fun insertData() {


    }

    private fun editData(dataKelas: Kelas) {
        TODO("Not yet implemented")
    }

    private fun setStatus(status: Boolean) {
        if (status) {

        } else {

        }
    }

    private fun setText(dataKelas: Kelas) {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}