package com.opasichnyi.beautify.data.entity

import com.google.gson.annotations.SerializedName

data class ApiCallResult<T, E>(
    @SerializedName("data") val data: T?,
    @SerializedName("error") val error: E?,
)