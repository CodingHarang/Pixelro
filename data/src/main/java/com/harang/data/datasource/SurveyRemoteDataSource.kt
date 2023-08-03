package com.harang.data.datasource

import com.harang.data.api.NenoonKioskApi
import com.harang.data.model.SendSurveyDataRequest
import com.harang.data.model.SendSurveyDataResponse

class SurveyRemoteDataSource(
    private val api: NenoonKioskApi
) {

    suspend fun sendSurveyData(
        request: SendSurveyDataRequest
    ): SendSurveyDataResponse? {
        return try {
            api.sendSurveyData(
                request
            ).body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}