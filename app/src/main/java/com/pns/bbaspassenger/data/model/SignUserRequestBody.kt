package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class SignUserRequestBody(
    @SerializedName("uid")
    val uid: String,

    @SerializedName("name")
    val name: String
)
