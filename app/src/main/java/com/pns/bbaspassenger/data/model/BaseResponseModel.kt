package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class BaseResponseModel<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("result")
    val result: T
)
