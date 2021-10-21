package com.pns.bbaspassenger.data.model

data class ReservationRouteItem(
    val nodeId: String,
    val nodeOrder: Int,
    val nodeName: String,
    val nodeNo: String,
    val isFirst: Boolean,
    val isLast: Boolean,
    var isStart: Boolean,
    var isDuring: Boolean,
    var isEnd: Boolean
)
