package com.harang.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SendMChartTestResultRequest(
    @SerializedName("leftEyeVertical")
    val leftEyeVertical: Int,
    @SerializedName("leftEyeHorizontal")
    val leftEyeHorizontal: Int,
    @SerializedName("rightEyeVertical")
    val rightEyeVertical: Int,
    @SerializedName("rightEyeHorizontal")
    val rightEyeHorizontal: Int
) : Serializable

data class SendMChartTestResultResponse(
    @SerializedName("result")
    val result: Int
) : Serializable