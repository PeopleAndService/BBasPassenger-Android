package com.pns.bbaspassenger.data.model

data class RouteItemModel(
    val nodeId: String,
    val nodeOrder: Int,
    val nodeName: String,
    val nodeNo: String,
    val nodeCnt: Int,
    val isStart: Boolean,
    val isEnd: Boolean,
    val isDuring: Boolean
) {
    val isFirst = nodeOrder == 1
    var isBusHere = false
}
