package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("uid")
    val userId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("pushToken")
    val pushToken: String?,

    @SerializedName("pushSetting")
    val pushSetting: Boolean,

    @SerializedName("emergencyPhone")
    val emergencyNumber: String?,

    @SerializedName("lfBusOption")
    val onlyLowBus: Boolean,

    @SerializedName("cityCode")
    val location: Int?
)
