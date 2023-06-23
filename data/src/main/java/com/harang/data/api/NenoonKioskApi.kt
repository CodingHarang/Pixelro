package com.harang.data.api

import com.harang.domain.model.TestData
import retrofit2.Response
import retrofit2.http.GET

interface NenoonKioskApi {
    @GET("test")
    suspend fun getTest(

    ): Response<TestData>
}