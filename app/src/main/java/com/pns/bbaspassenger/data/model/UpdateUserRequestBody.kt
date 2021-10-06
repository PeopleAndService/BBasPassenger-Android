package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class UpdateUserRequestBody(
    @SerializedName("uid")
    val userId: String,

    @SerializedName("emergencyPhone")
    val emergencyNumber: String,

    @SerializedName("lfBusOption")
    val onlyLowBus: Boolean,

    @SerializedName("cityCode")
    val location: Int
)
