package com.pns.bbaspassenger.data.source

import com.pns.bbaspassenger.data.model.BusArrivalInfo
import com.pns.bbaspassenger.data.model.BusBaseResponse
import com.pns.bbaspassenger.data.model.BusLocation
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

    // 노선별 버스 위치 목록
    @GET("BusLcInfoInqireService/getRouteAcctoBusLcList")
    suspend fun getBusLocation(
        @Query("ServiceKey") serviceKey: String,
        @Query("cityCode") cityCode: String,
        @Query("routeId") routeId: String,
        @Query("_type") _type: String = "json"
    ): Response<BusBaseResponse<List<BusLocation>>>

    // 정류소별 특정 노선 버스 도착 예정 정보 목록
    @GET("ArvlInfoInqireService/getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList")
    suspend fun getBusArrival(
        @Query("ServiceKey") serviceKey: String,
        @Query("cityCode") cityCode: String,
        @Query("nodeId") nodeId: String,
        @Query("routeId") routeId: String,
        @Query("_type") _type: String = "json"
    ): Response<BusBaseResponse<BusArrivalInfo>>
}