package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class Queue(
    @SerializedName("id")
    val id: Int,

    @SerializedName("uid")
    val userId: String,

    @SerializedName("stbusStopId")
    val startStationId: String,

    @SerializedName("edbusStopId")
    val endStationId: String,

    @SerializedName("stNodeOrder")
    val startStationOrder: Int,

    @SerializedName("edNodeOrder")
    val endStationOrder: Int,

    @SerializedName("busRouteId")
    val routeId: String,

    @SerializedName("vehicleId")
    val vehicleId: String,

    @SerializedName("boardingCheck")
    val isBoarded: Boolean
)
