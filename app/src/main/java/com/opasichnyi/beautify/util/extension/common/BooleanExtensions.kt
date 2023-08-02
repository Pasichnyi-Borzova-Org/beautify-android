package com.opasichnyi.beautify.util.extension.common

import androidx.annotation.CheckResult
import androidx.annotation.IntRange

@CheckResult
@IntRange(from = 0, to = 1)
fun Boolean?.toInt() = if (this == true) 1 else 0
