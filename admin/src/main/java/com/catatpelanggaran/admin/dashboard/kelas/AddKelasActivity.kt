package com.catatpelanggaran.admin.dashboard.kelas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Kelas
import kotlinx.android.synthetic.main.activity_add_guru.*
import kotlinx.android.synthetic.main.activity_add_guru.back_button
import kotlinx.android.synthetic.main.activity_add_guru.button_simpan
import kotlinx.android.synthetic.main.activity_add_guru.delete_button
import kotlinx.android.synthetic.main.activity_add_kelas.*

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

    private fun setText(dataKelas: Kelas?) {
        dataKelas?.let {
            input_tingkat.setText(dataKelas.tingkat)
            input_jurusan.setText(dataKelas.jurusan)

            if (dataKelas.kelas == "A") {
                kelas_a.isChecked = true
            } else if (dataKelas.kelas == "B") {
                kelas_B.isChecked = true
            } else if (dataKelas.kelas == "C") {
                kelas_c.isChecked = true
            } else {
                kelas_d.isChecked = true
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}