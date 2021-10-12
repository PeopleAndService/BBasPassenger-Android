package com.pns.bbaspassenger.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BusSystem(
    val id: String,

    val type: String,

    val name: String,

    val description: String,

    val busList: ArrayList<BusSystem>? = null
) : Parcelable