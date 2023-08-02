package com.opasichnyi.beautify.util.io

import android.content.Context
import android.net.Uri
import androidx.annotation.CheckResult
import androidx.core.content.FileProvider
import com.opasichnyi.beautify.BuildConfig
import com.opasichnyi.beautify.util.logger.LogToFileTree
import java.io.File

private const val LOG_FILE_NAME_PATTERN = LogToFileTree.LOG_FILE_NAME + "\\d+" + ".log$"

@CheckResult fun Context.getPathToLogFile(): List<Uri> =
    cacheDir
        .list()
        .orEmpty()
        .asSequence()
        .filter { Regex(LOG_FILE_NAME_PATTERN).matches(it) }
        .map { File(cacheDir, it) }
        .map { FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, it) }
        .toList()
