package com.pns.bbaspassenger.data.source

import com.pns.bbaspassenger.data.model.BaseResponseModel
import com.pns.bbaspassenger.data.model.BusSystem
import com.pns.bbaspassenger.data.model.CreateQueueRequestBody
import com.pns.bbaspassenger.data.model.GetStationRequestBody
import com.pns.bbaspassenger.data.model.GetUserRequestBody
import com.pns.bbaspassenger.data.model.PostRatingRequestBody
import com.pns.bbaspassenger.data.model.Queue
import com.pns.bbaspassenger.data.model.SearchRequestBody
import com.pns.bbaspassenger.data.model.SignUserRequestBody
import com.pns.bbaspassenger.data.model.UpdateQueueRequestBody
import com.pns.bbaspassenger.data.model.UpdateUserRequestBody
import com.pns.bbaspassenger.data.model.User
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BBasService {

    //user
    @POST("passengerSign")
    suspend fun sign(
        @Body userRequestBody: SignUserRequestBody
    ): Response<BaseResponseModel<User>>

    @POST("passenger")
    suspend fun getUser(
        @Body getUserRequestBody: GetUserRequestBody
    ): Response<BaseResponseModel<User>>

    @PUT("passenger")
    suspend fun updateUser(
        @Body updateUserRequestBody: UpdateUserRequestBody
    ): Response<BaseResponseModel<User>>

    @HTTP(method = "DELETE", path = "passenger", hasBody = true)
    suspend fun deleteUser(
        @Body deleteUserRequestBody: GetUserRequestBody
    ): Response<BaseResponseModel<JSONObject>>

    // queue
    @GET("queue/{uid}")
    suspend fun getQueue(
        @Path("uid") userId: String
    ): Response<BaseResponseModel<Queue>>

    @POST("queue/0")
    suspend fun createQueue(
        @Body createQueueRequestBody: CreateQueueRequestBody
    ): Response<BaseResponseModel<Queue>>

    @PUT("queue/0")
    suspend fun updateQueue(
        @Body updateQueueRequestBody: UpdateQueueRequestBody
    ): Response<BaseResponseModel<Queue>>

    @HTTP(method = "DELETE", path = "queue/0", hasBody = true)
    suspend fun deleteQueue(
        @Body deleteUserRequestBody: GetUserRequestBody
    ): Response<BaseResponseModel<JSONObject>>

    // rating
    @POST("rating/0")
    suspend fun createRating(
        @Body postRatingRequestBody: PostRatingRequestBody
    ): Response<BaseResponseModel<JSONObject>>

    // station
    @POST("getStationInfo")
    suspend fun getStation(
        @Body getStationBody: GetStationRequestBody
    ): Response<BaseResponseModel<List<BusSystem>>>

    // search
    @POST("search")
    suspend fun search(
        @Body searchRequestBody: SearchRequestBody
    ): Response<BaseResponseModel<List<BusSystem>>>
}