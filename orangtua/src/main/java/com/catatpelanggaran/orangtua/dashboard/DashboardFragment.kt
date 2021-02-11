package com.catatpelanggaran.orangtua.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adriandery.catatpelanggaran.LoginActivity
import com.adriandery.catatpelanggaran.SharedPreferences
import com.catatpelanggaran.orangtua.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    lateinit var nis: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nis = activity?.intent?.getStringExtra("NIS").toString()

        getData(nis)
    }

    private fun getData(nis: String) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("Siswa").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val namaKelas = snapshot.child(nis).child("nama_siswa").value.toString()
                nama_siswa.text = namaKelas
                nis_siswa.text = nis
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
}