package com.catatpelanggaran.gurubk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Pelanggaran(
    val idPelanggaran: String? = null,
    val namaPelanggaran: String? = null,
    val poinPelanggaran: Int? = null
) : Parcelable