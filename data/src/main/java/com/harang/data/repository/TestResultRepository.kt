package com.harang.data.repository

import com.harang.data.datasource.TestResultRemoteDataSource
import com.harang.data.model.SendAmslerGridTestResultRequest
import com.harang.data.model.SendAmslerGridTestResultResponse
import com.harang.data.model.SendMChartTestResultRequest
import com.harang.data.model.SendMChartTestResultResponse
import com.harang.data.model.SendPresbyopiaTestResultRequest
import com.harang.data.model.SendPresbyopiaTestResultResponse
import com.harang.data.model.SendShortVisualAcuityTestResultRequest
import com.harang.data.model.SendShortVisualAcuityTestResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TestResultRepository(
    private val remoteDataSource: TestResultRemoteDataSource
) {

    suspend fun sendPresbyopiaTestResult(
        request: SendPresbyopiaTestResultRequest
    ): SendPresbyopiaTestResultResponse? {
        return withContext(Dispatchers.IO) {
            remoteDataSource.sendPresbyopiaTestResult(request)
        }
    }

    suspend fun sendVisualAcuityTestResult(
        request: SendShortVisualAcuityTestResultRequest
    ): SendShortVisualAcuityTestResultResponse? {
        return withContext(Dispatchers.IO) {
            remoteDataSource.sendVisualAcuityTestResult(request)
        }
    }

    suspend fun sendAmslerGridTestResult(
        request: SendAmslerGridTestResultRequest
    ): SendAmslerGridTestResultResponse? {
        return withContext(Dispatchers.IO) {
            remoteDataSource.sendAmslerGridTestResult(request)
        }
    }

    suspend fun sendMChartTestResult(
        request: SendMChartTestResultRequest
    ): SendMChartTestResultResponse? {
        return withContext(Dispatchers.IO) {
            remoteDataSource.sendMChartTestResult(request)
        }
    }
}