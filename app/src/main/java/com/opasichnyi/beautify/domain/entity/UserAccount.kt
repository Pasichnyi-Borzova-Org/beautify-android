package com.opasichnyi.beautify.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// TODO("Refactor to Sealed Class with master and client")
@Parcelize
data class UserAccount(
    val login: String,
    val name: String,
    val surname: String?,
    val role: UserRole,
) : Parcelable