package com.pns.bbaspassenger.repository

import com.pns.bbaspassenger.data.model.CreateQueueRequestBody
import com.pns.bbaspassenger.data.source.RemoteDataSource

object ReservationRepository {
    private val service = RemoteDataSource.busService

    suspend fun getRoute(serviceKey: String, cityCode: String, routeId: String) =
        service.getRoute(serviceKey, cityCode, routeId)

    suspend fun getBusLocation(serviceKey: String, cityCode: String, routeId: String) =
        service.getBusLocation(serviceKey, cityCode, routeId)

    suspend fun createReservation(requestBody: CreateQueueRequestBody) =
        RemoteDataSource.bbasService.createQueue(requestBody)
}