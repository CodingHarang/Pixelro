package com.harang.data.api

import com.harang.data.model.SendAmslerGridTestResultRequest
import com.harang.data.model.SendAmslerGridTestResultResponse
import com.harang.data.model.SendMChartTestResultRequest
import com.harang.data.model.SendMChartTestResultResponse
import com.harang.data.model.SendPresbyopiaTestResultRequest
import com.harang.data.model.SendPresbyopiaTestResultResponse
import com.harang.data.model.SendShortVisualAcuityTestResultRequest
import com.harang.data.model.SendShortVisualAcuityTestResultResponse
import com.harang.data.model.SendSignInDataResponse
import com.harang.data.model.SendSurveyDataRequest
import com.harang.data.model.SendSurveyDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NenoonKioskApi {

    @POST("api/v1/test/result/amsler")
    suspend fun sendAmslerGridResult(
        @Body body: SendAmslerGridTestResultRequest
    ): Response<SendAmslerGridTestResultResponse>

    @POST("api/v1/test/result/presbyopia")
    suspend fun sendPresbyopiaTestResult(
        @Body body: SendPresbyopiaTestResultRequest
    ): Response<SendPresbyopiaTestResultResponse>

    @POST("api/v1/test/result/mCharts")
    suspend fun sendMChartTestResult(
        @Body body: SendMChartTestResultRequest
    ): Response<SendMChartTestResultResponse>

    @POST("api/v1/test/result/sight")
    suspend fun sendShortVisualAcuityTestResult(
        @Body body: SendShortVisualAcuityTestResultRequest
    ): Response<SendShortVisualAcuityTestResultResponse>

    @POST("api/v1/survey")
    suspend fun sendSurveyData(
        @Body body: SendSurveyDataRequest
    ): Response<SendSurveyDataResponse>

    @GET("api/v1/location/signin")
    suspend fun sendSignInData(
        @Query("id") id: String,
        @Query("pw") pw: String
    ): Response<SendSignInDataResponse>
}