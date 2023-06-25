package com.harang.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SendPresbyopiaTestResultRequest(
    @SerializedName("firstDistance")
    val firstDistance: Float,
    @SerializedName("secondDistance")
    val secondDistance: Float,
    @SerializedName("thirdDistance")
    val thirdDistance: Float
) : Serializable

data class SendPresbyopiaTestResultResponse(
    @SerializedName("result")
    val result: Int
) : Serializable