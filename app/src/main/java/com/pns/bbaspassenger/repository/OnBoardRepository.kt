package com.pns.bbaspassenger.repository

import com.pns.bbaspassenger.data.model.GetUserRequestBody
import com.pns.bbaspassenger.data.model.PostRatingRequestBody
import com.pns.bbaspassenger.data.model.UpdateQueueRequestBody
import com.pns.bbaspassenger.data.source.RemoteDataSource

object OnBoardRepository {
    suspend fun getQueue(userId: String) = RemoteDataSource.bbasService.getQueue(userId)
    suspend fun getRoute(serviceKey: String, cityCode: String, routeId: String) = RemoteDataSource.busService.getRoute(serviceKey, cityCode, routeId)
    suspend fun getBusPosition(serviceKey: String, cityCode: String, routeId: String) = RemoteDataSource.busService.getBusLocation(serviceKey, cityCode, routeId)
    suspend fun getBusArrival(serviceKey: String, cityCode: String, nodeId: String, routeId: String) =
        RemoteDataSource.busService.getBusArrival(serviceKey, cityCode, nodeId, routeId)

    suspend fun updateQueue(updateQueueRequestBody: UpdateQueueRequestBody) = RemoteDataSource.bbasService.updateQueue(updateQueueRequestBody)
    suspend fun deleteQueue(deleteQueueRequestBody: GetUserRequestBody) = RemoteDataSource.bbasService.deleteQueue(deleteQueueRequestBody)

    suspend fun createRating(postRatingRequestBody: PostRatingRequestBody) = RemoteDataSource.bbasService.createRating(postRatingRequestBody)
}