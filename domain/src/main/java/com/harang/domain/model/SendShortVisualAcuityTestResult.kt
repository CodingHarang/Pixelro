package com.harang.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SendShortVisualAcuityTestResultRequest(
    @SerializedName("leftEye")
    val leftEye: Int,
    @SerializedName("rightEye")
    val rightEye: Int
) : Serializable

data class SendShortVisualAcuityTestResultResponse(
    @SerializedName("result")
    val result: Int
) : Serializable