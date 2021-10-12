package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class BusArrivalInfo(
    @SerializedName("routeid")
    val routeId: String,

    @SerializedName("nodeid")
    val nodeId: String,

    @SerializedName("nodenm")
    val nodeName: String,

    @SerializedName("routeno")
    val routeNo: String,

    @SerializedName("arrprevstationcnt")
    val prevCnt: String,

    @SerializedName("vehicletp")
    val vehicleType: String,

    @SerializedName("arrtime")
    val remainTime: String
)
