package com.catatpelanggaran.gurubk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Catat(
    val tanggal: String? = null,
    val nis: String? = null,
    val nama_siswa: String? = null,
    val telp_ortu: String? = null,
    val namaPelanggaran: String? = null,
    val keterangan: String? = null,
    val poinPelanggaran: Int? = null,
    val id_kelas: String? = null,
    val jenkel: String? = null,
    val alamat: String? = null,
    val idPelanggaran: String? = null

) : Parcelable