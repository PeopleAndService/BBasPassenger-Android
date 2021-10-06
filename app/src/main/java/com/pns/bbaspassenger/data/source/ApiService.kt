package com.pns.bbaspassenger.data.source

import com.pns.bbaspassenger.data.model.BaseResponseModel
import com.pns.bbaspassenger.data.model.GetUserRequestBody
import com.pns.bbaspassenger.data.model.SignUpRequestBody
import com.pns.bbaspassenger.data.model.UpdateUserRequestBody
import com.pns.bbaspassenger.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    //user
    @POST("passengerSign")
    suspend fun sign(
        @Body userRequestBody: SignUpRequestBody
    ): Response<BaseResponseModel<User>>

    @POST("passenger")
    suspend fun getUser(
        @Body getUserRequestBody: GetUserRequestBody
    ): Response<BaseResponseModel<User>>

    @PUT("passenger")
    suspend fun updateUser(
        @Body updateUserRequestBody: UpdateUserRequestBody
    ): Response<BaseResponseModel<User>>
}