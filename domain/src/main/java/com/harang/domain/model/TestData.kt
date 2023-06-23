package com.harang.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TestData(
    @SerializedName("data1")
    val data1: Int = 1,
    @SerializedName("data2")
    val data2: String = "data2"
) : Serializable
