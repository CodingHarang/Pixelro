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

}