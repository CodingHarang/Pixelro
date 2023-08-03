package com.harang.data.datasource

import com.harang.data.api.NenoonKioskApi
import com.harang.data.model.SendAmslerGridTestResultRequest
import com.harang.data.model.SendAmslerGridTestResultResponse
import com.harang.data.model.SendMChartTestResultRequest
import com.harang.data.model.SendMChartTestResultResponse
import com.harang.data.model.SendPresbyopiaTestResultRequest
import com.harang.data.model.SendPresbyopiaTestResultResponse
import com.harang.data.model.SendShortVisualAcuityTestResultRequest
import com.harang.data.model.SendShortVisualAcuityTestResultResponse

class TestResultRemoteDataSource(
    private val api: NenoonKioskApi
) {

    suspend fun sendPresbyopiaTestResult(
        request: SendPresbyopiaTestResultRequest
    ): SendPresbyopiaTestResultResponse? {
        return try {
            api.sendPresbyopiaTestResult(request).body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun sendVisualAcuityTestResult(
        request: SendShortVisualAcuityTestResultRequest
    ): SendShortVisualAcuityTestResultResponse? {
        return try {
            api.sendShortVisualAcuityTestResult(request).body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun sendAmslerGridTestResult(
        request: SendAmslerGridTestResultRequest
    ): SendAmslerGridTestResultResponse? {
        return try {
            api.sendAmslerGridResult(request).body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun sendMChartTestResult(
        request: SendMChartTestResultRequest
    ): SendMChartTestResultResponse? {
        return try {
            api.sendMChartTestResult(request).body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}