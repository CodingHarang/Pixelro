package com.harang.data.repository

import android.content.SharedPreferences
import com.harang.data.datasource.SharedPreferencesDataSource
import com.harang.data.datasource.SignInRemoteDataSource
import com.harang.data.model.SendSignInDataResponse
import com.harang.data.vo.Constants
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
            sharedPreferencesDataSource.putInt(Constants.PREF_LOCATION_ID, locationId)
        }
    }

    suspend fun updateScreenSaverVideoURI(uri: String) {
        withContext(Dispatchers.IO) {
            sharedPreferencesDataSource.putString(Constants.PREF_VIDEO_URI, uri)
        }
    }

    suspend fun signOut() {
        withContext(Dispatchers.IO) {
            sharedPreferencesDataSource.removeKeyValue(Constants.PREF_LOCATION_ID)
            sharedPreferencesDataSource.removeKeyValue(Constants.PREF_VIDEO_URI)
        }
    }
}