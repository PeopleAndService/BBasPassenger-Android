package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class PostRatingRequestBody(
    @SerializedName("vehicleId")
    val vehicleId: String,

    @SerializedName("ratingData")
    val ratingData: Double
)
