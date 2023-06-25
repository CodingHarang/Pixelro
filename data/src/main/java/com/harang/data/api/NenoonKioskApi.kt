package com.harang.data.api

import com.harang.domain.model.SendAmslerGridTestResultRequest
import com.harang.domain.model.SendAmslerGridTestResultResponse
import com.harang.domain.model.SendMChartTestResultRequest
import com.harang.domain.model.SendMChartTestResultResponse
import com.harang.domain.model.SendPresbyopiaTestResultRequest
import com.harang.domain.model.SendPresbyopiaTestResultResponse
import com.harang.domain.model.SendShortVisualAcuityTestResultRequest
import com.harang.domain.model.SendShortVisualAcuityTestResultResponse
import com.harang.domain.model.TestData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NenoonKioskApi {
    @GET("test")
    suspend fun getTest(

    ): Response<TestData>

    @POST("amslergridresult")
    suspend fun sendAmslerGridResult(
        @Body body: SendAmslerGridTestResultRequest
    ): Response<SendAmslerGridTestResultResponse>

    @POST("presbyopiatestresult")
    suspend fun sendPresbyopiaTestResult(
        @Body body: SendPresbyopiaTestResultRequest
    ): Response<SendPresbyopiaTestResultResponse>

    @POST("mcharttestresult")
    suspend fun sendMChartTestResult(
        @Body body: SendMChartTestResultRequest
    ): Response<SendMChartTestResultResponse>

    @POST("shortvisualacuitytestresult")
    suspend fun sendShortVisualAcuityTestResult(
        @Body body: SendShortVisualAcuityTestResultRequest
    ): Response<SendShortVisualAcuityTestResultResponse>

}