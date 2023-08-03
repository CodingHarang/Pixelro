package com.harang.data.repository

import com.harang.data.datasource.SharedPreferencesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScreenSaverRepository(
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) {
    suspend fun getScreenSaverVideoURI(): String {
        val videoURI: String
        withContext(Dispatchers.IO) {
            videoURI = sharedPreferencesDataSource.getString("screenSaverVideoURI")
        }
        return videoURI
    }
}