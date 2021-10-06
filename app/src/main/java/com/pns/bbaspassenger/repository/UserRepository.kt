package com.pns.bbaspassenger.repository

import com.pns.bbaspassenger.data.model.SignUpRequestBody
import com.pns.bbaspassenger.data.model.GetUserRequestBody
import com.pns.bbaspassenger.data.source.RemoteDataSource

object UserRepository {
    suspend fun sign(userRequestBody: SignUpRequestBody) = RemoteDataSource.bbasService.sign(userRequestBody)
    suspend fun getUser(getUserRequestBody: GetUserRequestBody) = RemoteDataSource.bbasService.getUser(getUserRequestBody)
}