package com.pns.bbaspassenger.repository

import com.pns.bbaspassenger.data.model.GetStationRequestBody
import com.pns.bbaspassenger.data.model.SearchRequestBody
import com.pns.bbaspassenger.data.source.RemoteDataSource

object BusSystemRepository {
    private val service = RemoteDataSource.bbasService

    suspend fun getStation(getStationRequestBody: GetStationRequestBody) = service.getStation(getStationRequestBody)
    suspend fun search(searchRequestBody: SearchRequestBody) = service.search(searchRequestBody)
}