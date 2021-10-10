package com.pns.bbaspassenger.repository

import com.pns.bbaspassenger.data.source.RemoteDataSource

object OnBoardRepository {
    suspend fun getQueue(userId: String) = RemoteDataSource.bbasService.getQueue(userId)
    suspend fun getRoute(serviceKey: String, cityCode: String, routeId: String) = RemoteDataSource.busService.getRoute(serviceKey, cityCode, routeId)
}