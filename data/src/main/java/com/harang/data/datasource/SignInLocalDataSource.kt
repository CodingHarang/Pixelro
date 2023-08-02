package com.harang.data.datasource

import com.harang.data.api.NenoonKioskApi
import com.harang.data.model.SendSignInDataResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Response

class SignInLocalDataSource(
    private val api: NenoonKioskApi
) {
    suspend fun signIn(id: String, pw: String): SendSignInDataResponse {
        CoroutineScope(Dispatchers.IO).launch {
            val response = try {
                api.sendSignInData(
                    id = id,
                    pw = pw
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}