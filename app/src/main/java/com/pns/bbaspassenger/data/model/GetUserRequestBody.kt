package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class GetUserRequestBody(
    @SerializedName("uid")
    val uid: String
)
