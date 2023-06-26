package com.harang.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SendPresbyopiaTestResultRequest(
    @SerializedName("firstDistance")
    val firstDistance: Float,
    @SerializedName("secondDistance")
    val secondDistance: Float,
    @SerializedName("thirdDistance")
    val thirdDistance: Float,
    @SerializedName("avgDistance")
    val avgDistance: Float,
    @SerializedName("age")
    val age: Int
) : Serializable

data class SendPresbyopiaTestResultResponse(
    @SerializedName("result")
    val result: Int,
    @SerializedName("firstDistance")
    val firstDistance: Float,
    @SerializedName("secondDistance")
    val secondDistance: Float,
    @SerializedName("thirdDistance")
    val thirdDistance: Float,
    @SerializedName("avgDistance")
    val avgDistance: Float,
    @SerializedName("age")
val age: Int
) : Serializable