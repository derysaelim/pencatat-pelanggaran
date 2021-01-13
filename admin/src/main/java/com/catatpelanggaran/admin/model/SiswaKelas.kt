package com.catatpelanggaran.admin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SiswaKelas(
    val nis: String? = null,
    val nama_siswa: String? = null
) : Parcelable