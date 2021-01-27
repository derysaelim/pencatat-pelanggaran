package com.adriandery.catatpelanggaran.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ortu(
    val id: String? = null,
    val nama: String? = null,
    val nohp: String? = null
) : Parcelable