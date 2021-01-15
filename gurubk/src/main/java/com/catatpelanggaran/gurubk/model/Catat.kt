package com.catatpelanggaran.gurubk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Catat(
    val nis: String? = null,
    val nama_siswa: String? = null,
    val tanggal: String? = null,
    val namaPelanggaran: String? = null,
    val keterangan: String? = null,
    val hukuman: String? = null,
    val poinPelanggaran: Int = 0
) : Parcelable