package com.pns.bbaspassenger.data.source

import com.pns.bbaspassenger.data.model.BaseResponseModel
import com.pns.bbaspassenger.data.model.GetUserRequestBody
import com.pns.bbaspassenger.data.model.SignUserRequestBody
import com.pns.bbaspassenger.data.model.UpdateUserRequestBody
import com.pns.bbaspassenger.data.model.User
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

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
}