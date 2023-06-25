package com.harang.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SendAmslerGridTestResultRequest(
    @SerializedName("leftEyeDisorderType")
    val leftEyeDisorderType: String,
    @SerializedName("rightEyeDisorderType")
    val rightEyeDisorderType: String
) : Serializable

data class SendAmslerGridTestResultResponse(
    @SerializedName("result")
    val result: Int,
    @SerializedName("leftEyeDisorderType")
    val leftEyeDisorderType: String,
    @SerializedName("rightEyeDisorderType")
    val rightEyeDisorderType: String
) : Serializable