package com.catatpelanggaran.gurubk.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.dashboard.catat.CatatPelanggaranActivity
import com.catatpelanggaran.gurubk.dashboard.datapelanggar.DataPelanggarActivity
import com.catatpelanggaran.gurubk.dashboard.pelanggaran.PelanggaranActivity
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

        catat_pelanggaran.setOnClickListener(this)
        data_pelanggaran.setOnClickListener(this)
        buat_laporan.setOnClickListener(this)
        data_pelanggar.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        lateinit var intent: Intent

        when (view.id){
            R.id.catat_pelanggaran -> {
                intent = Intent(context, CatatPelanggaranActivity::class.java)
            }
            R.id.data_pelanggaran -> {
                intent = Intent(context, PelanggaranActivity::class.java)
            }
            R.id.buat_laporan -> {
                intent = Intent(context, DataPelanggarActivity::class.java)
            }
            R.id.data_pelanggar -> {
                intent = Intent(context, DataPelanggarActivity::class.java)
            }
        }
        startActivity(intent)

    }
}