package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class RouteStation(
    @SerializedName("nodeid")
    val nodeId: String,

    @SerializedName("nodenm")
    val nodeName: String,

    @SerializedName("nodeno")
    val nodeNo: String,

    @SerializedName("nodeord")
    val nodeOrder: Int
)
