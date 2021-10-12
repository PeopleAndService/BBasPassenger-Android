package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class UpdateQueueRequestBody(
    @SerializedName("uid")
    val userId: String,

    @SerializedName("boardingCheck")
    val status: Int
)
