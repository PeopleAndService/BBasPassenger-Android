package com.pns.bbaspassenger.repository

import com.pns.bbaspassenger.data.model.SignUserRequestBody
import com.pns.bbaspassenger.data.model.GetUserRequestBody
import com.pns.bbaspassenger.data.model.UpdateUserRequestBody
import com.pns.bbaspassenger.data.source.RemoteDataSource

object UserRepository {
    private val service = RemoteDataSource.bbasService

    suspend fun sign(userRequestBody: SignUserRequestBody) = service.sign(userRequestBody)
    suspend fun getUser(getUserRequestBody: GetUserRequestBody) = service.getUser(getUserRequestBody)
    suspend fun updateUser(updateUserRequestBody: UpdateUserRequestBody) = service.updateUser(updateUserRequestBody)
    suspend fun deleteUser(deleteUserRequestBody: GetUserRequestBody) = service.deleteUser(deleteUserRequestBody)
}