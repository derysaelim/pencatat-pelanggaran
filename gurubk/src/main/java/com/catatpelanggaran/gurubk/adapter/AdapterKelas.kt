package com.catatpelanggaran.gurubk.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Kelas
import com.catatpelanggaran.gurubk.model.Siswa
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_kelas.view.*
import kotlinx.android.synthetic.main.item_siswa.view.*
import kotlinx.android.synthetic.main.item_siswa.view.no_absen

class AdapterKelas(val kelas: ArrayList<Siswa>) : RecyclerView.Adapter<AdapterKelas.ViewHolder>() {

    var onItemClick: ((Siswa) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterKelas.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kelas, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = kelas.size

    override fun onBindViewHolder(holder: AdapterKelas.ViewHolder, position: Int) {
        holder.bind(kelas[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(dataSiswa: Siswa) {
            with(itemView) {
                val absen = position + 1
                val kelas = "${dataSiswa.tingkat} ${dataSiswa.jurusan} ${dataSiswa.kelas}"

                no_absenkel.text = absen.toString()
                nama_kelas.text = kelas

                val database = FirebaseDatabase.getInstance().reference
                database.child("Guru").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        nama_walikelas.text =
                            snapshot.child(dataSiswa.nip!!).child("nama").value.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        nama_walikelas.text = "---"
                    }

                })
            }
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(kelas[adapterPosition])
            }
        }

    }



}