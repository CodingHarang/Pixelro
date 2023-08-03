package com.harang.data.repository

import android.content.SharedPreferences
import com.harang.data.datasource.SharedPreferencesDataSource
import com.harang.data.datasource.SignInRemoteDataSource
import com.harang.data.model.SendSignInDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignInRepository(
    private val remoteDataSource: SignInRemoteDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) {
    suspend fun getSignInResult(id: String, pw: String): SendSignInDataResponse? {
        return withContext(Dispatchers.IO) {
            remoteDataSource.signIn(id, pw)
        }
    }

    suspend fun updateLocationId(locationId: Int) {
        withContext(Dispatchers.IO) {
            sharedPreferencesDataSource.putInt("locationId", locationId)
        }
    }

    suspend fun updateScreenSaverVideoURI(uri: String) {
        withContext(Dispatchers.IO) {
            sharedPreferencesDataSource.putString("screenSaverVideoURI", uri)
        }
    }
}