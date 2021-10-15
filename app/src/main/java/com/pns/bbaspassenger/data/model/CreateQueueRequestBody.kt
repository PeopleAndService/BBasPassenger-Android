package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class CreateQueueRequestBody(
    @SerializedName("stbusStopId")
    val startBusStopId: String,

    @SerializedName("edbusStopId")
    val endBusStopId: String,

    @SerializedName("busRouteId")
    val busRouteId: String,

    @SerializedName("vehicleId")
    val vehicleId: String,

    @SerializedName("uid")
    val uid: String,

    @SerializedName("stNodeOrder")
    val startNodeOrder: Int,

    @SerializedName("edNodeOrder")
    val endNodeOrder: Int
)
