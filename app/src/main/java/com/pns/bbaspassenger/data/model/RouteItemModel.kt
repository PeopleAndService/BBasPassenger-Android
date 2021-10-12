package com.pns.bbaspassenger.data.model

data class RouteItemModel(
    val nodeId: String,
    val nodeOrder: Int,
    val nodeName: String,
    val nodeNo: String,
    var isStart: Boolean,
    var isEnd: Boolean,
    var isDuring: Boolean,
    val isFirst: Boolean,
    var isBusHere: Boolean,
    var remainSec: Int,
    var remainCnt: Int
)
