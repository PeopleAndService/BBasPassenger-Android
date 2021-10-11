package com.pns.bbaspassenger.data.model

data class RouteQueueInfo(
    val first: Int,
    val last: Int,
    val userStart: Int,
    val userEnd: Int,
    var busPosition: Int
)
