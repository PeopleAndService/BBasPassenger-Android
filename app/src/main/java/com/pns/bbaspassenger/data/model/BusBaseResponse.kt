package com.pns.bbaspassenger.data.model

import com.google.gson.annotations.SerializedName

data class BusBaseResponse<T>(
    @SerializedName("response")
    val response: BusResponse<T>
)

data class BusResponse<T>(
    @SerializedName("header")
    val header: BusHeader,

    @SerializedName("body")
    val body: BusBody<T>
)

data class BusHeader(
    @SerializedName("resultCode")
    val resultCode: String,

    @SerializedName("resultMsg")
    val resultMsg: String
)

data class BusBody<T>(
    @SerializedName("items")
    val items: BusItems<T>,

    @SerializedName("numOfRows")
    val numOfRows: Int,

    @SerializedName("pageNo")
    val pageNo: Int,

    @SerializedName("totalCount")
    val totalCount: Int
)

data class BusItems<T>(
    @SerializedName("item")
    val result: T
)