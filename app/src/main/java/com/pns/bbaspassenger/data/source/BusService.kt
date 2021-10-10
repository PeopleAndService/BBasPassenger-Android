package com.pns.bbaspassenger.data.source

import com.pns.bbaspassenger.data.model.BusBaseResponse
import com.pns.bbaspassenger.data.model.RouteStation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BusService {

    // 노선별 경유 정류소 목록
    @GET("BusRouteInfoInqireService/getRouteAcctoThrghSttnList")
    suspend fun getRoute(
        @Query("ServiceKey") serviceKey: String,
        @Query("cityCode") cityCode: String,
        @Query("routeId") routeId: String,
        @Query("numOfRows") numOfRows: String = "100",
        @Query("_type") _type: String = "json"
    ): Response<BusBaseResponse<List<RouteStation>>>
}