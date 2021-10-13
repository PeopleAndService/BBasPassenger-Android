package com.pns.bbaspassenger.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BusSystem(
    val id: String,

    val type: String,

    val name: String,

    val description: String,

    @SerializedName("routeData")
    val busList: ArrayList<BusSystem>? = null
) : Parcelable