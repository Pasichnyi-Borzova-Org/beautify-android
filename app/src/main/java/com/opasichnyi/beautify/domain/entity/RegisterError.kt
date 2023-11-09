package com.opasichnyi.beautify.domain.entity

import com.google.gson.annotations.SerializedName

enum class RegisterError(val value: String){
    @SerializedName("WEAK_PASSWORD")
    WEAK_PASSWORD("WEAK_PASSWORD"),
    @SerializedName("USERNAME_ALREADY_EXISTS")
    USERNAME_ALREADY_EXISTS("USERNAME_ALREADY_EXISTS"),
    @SerializedName("REQUIRED_FIELDS_ARE_MISSING")
    REQUIRED_FIELDS_ARE_MISSING("REQUIRED_FIELDS_ARE_MISSING"),
}