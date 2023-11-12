package com.opasichnyi.beautify.data.entity

import com.google.gson.annotations.SerializedName
import com.opasichnyi.beautify.domain.entity.UserRole

data class DataRegisterData (
    val username: String,
    val name: String,
    val surname: String,
    val city: String?,
    val password: String,
    @SerializedName("is_master")
    val isMaster: Int,
)