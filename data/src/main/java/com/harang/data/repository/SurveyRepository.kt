package com.harang.data.repository

import com.harang.data.datasource.SignInRemoteDataSource
import com.harang.data.datasource.SurveyRemoteDataSource
import com.harang.data.model.SendSurveyDataRequest
import com.harang.data.model.SendSurveyDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SurveyRepository(
    private val remoteDataSource: SurveyRemoteDataSource
) {

    suspend fun sendSurveyData(request: SendSurveyDataRequest): SendSurveyDataResponse? {
        return withContext(Dispatchers.IO) {
            remoteDataSource.sendSurveyData(request)
        }
    }
}