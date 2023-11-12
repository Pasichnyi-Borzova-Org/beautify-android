package com.opasichnyi.beautify.domain.entity

import com.google.gson.annotations.SerializedName

enum class AppointmentCreationError(val value: String) {
    @SerializedName("REQUIRED_FIELDS_ARE_MISSING")
    REQUIRED_FIELDS_ARE_MISSING("REQUIRED_FIELDS_ARE_MISSING"),
    @SerializedName("TIME_IS_OCCUPIED")
    TIME_IS_OCCUPIED("TIME_IS_OCCUPIED"),
    @SerializedName("DB_INSERT_FAILED")
    DB_INSERT_FAILED("DB_INSERT_FAILED"),
}