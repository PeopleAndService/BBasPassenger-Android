package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class BusLocation(
    @SerializedName("nodeord")
    val nodeOrder: Int,

    @SerializedName("nodenm")
    val nodeName: String,

    @SerializedName("nodeid")
    val nodeId: String,

    @SerializedName("vehicleno")
    val vehicleId: String
)
