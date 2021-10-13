package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class SearchRequestBody(
    @SerializedName("gpsLati")
    val latitude: Double,

    @SerializedName("gpsLong")
    val longitude: Double,

    @SerializedName("keyword")
    val query: String
)
