package com.harang.data.datasource

import com.harang.data.api.NenoonKioskApi
import com.harang.data.model.SendSignInDataResponse

class SignInRemoteDataSource(
    private val api: NenoonKioskApi
) {
    suspend fun signIn(id: String, pw: String): SendSignInDataResponse? {
        return try {
            api.sendSignInData(
                id = id,
                pw = pw
            ).body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}