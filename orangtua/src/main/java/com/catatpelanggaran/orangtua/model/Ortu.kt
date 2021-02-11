package com.catatpelanggaran.orangtua.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ortu(
    val nama: String? = null,
    val nis: String? = null,
    val nohp: String? = null
) : Parcelable