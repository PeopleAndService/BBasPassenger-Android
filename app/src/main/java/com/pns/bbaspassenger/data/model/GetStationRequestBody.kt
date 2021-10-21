package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class GetStationRequestBody(
    @SerializedName("gpsLati")
    val latitude: Double,

    @SerializedName("gpsLong")
    val longitude: Double
)
