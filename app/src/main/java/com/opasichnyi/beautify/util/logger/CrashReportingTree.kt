package com.opasichnyi.beautify.util.logger

import android.util.Log
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        if (priority == Log.ERROR) {
            TODO("Implement logic for crash reporting")
        }
    }
}
