package com.pns.bbaspassenger.repository

import com.pns.bbaspassenger.data.model.SignUserRequestBody
import com.pns.bbaspassenger.data.model.GetUserRequestBody
import com.pns.bbaspassenger.data.model.UpdateUserRequestBody
import com.pns.bbaspassenger.data.source.RemoteDataSource

object UserRepository {
    suspend fun sign(userRequestBody: SignUserRequestBody) = RemoteDataSource.bbasService.sign(userRequestBody)
    suspend fun getUser(getUserRequestBody: GetUserRequestBody) = RemoteDataSource.bbasService.getUser(getUserRequestBody)
    suspend fun updateUser(updateUserRequestBody: UpdateUserRequestBody) = RemoteDataSource.bbasService.updateUser(updateUserRequestBody)
}