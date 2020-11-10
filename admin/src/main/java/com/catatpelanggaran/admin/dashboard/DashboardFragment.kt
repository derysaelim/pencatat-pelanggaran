package com.catatpelanggaran.admin.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.dashboard.guru.GuruActivity
import com.catatpelanggaran.admin.dashboard.kelas.KelasActivity
import com.catatpelanggaran.admin.dashboard.pelanggaran.PelanggaranActivity
import com.catatpelanggaran.admin.dashboard.siswa.SiswaActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_siswa.setOnClickListener(this)
        button_guru.setOnClickListener(this)
        button_kelas.setOnClickListener(this)
        button_pelanggaran.setOnClickListener(this)

    }

    override fun onClick(view: View) {

        lateinit var intent: Intent

        when (view.id) {
            R.id.button_siswa -> {
                intent = Intent(context, SiswaActivity::class.java)
            }
            R.id.button_guru -> {
                intent = Intent(context, GuruActivity::class.java)
            }
            R.id.button_kelas -> {
                intent = Intent(context, KelasActivity::class.java)
            }
            R.id.button_pelanggaran -> {
                intent = Intent(context, PelanggaranActivity::class.java)
            }
        }

        startActivity(intent)
    }

}