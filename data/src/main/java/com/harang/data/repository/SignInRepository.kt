package com.harang.data.repository

import com.harang.data.datasource.SignInRemoteDataSource
import com.harang.data.model.SendSignInDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignInRepository(
    private val source: SignInRemoteDataSource
) {
    suspend fun getSignInResult(id: String, pw: String): SendSignInDataResponse? {
        return withContext(Dispatchers.IO) {source.signIn(id, pw) }
    }
}