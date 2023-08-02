package com.opasichnyi.beautify.util.logger

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.opasichnyi.beautify.BuildConfig
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.LogRecord

class LogToFileTree(context: Context) : Timber.Tree() {

    private val internalStorageCache: File = context.cacheDir

    private val fileHandler = FileHandler(
        internalStorageCache.toString() + File.separator + LOG_FILES_TEMPLATE,
        SIZE_LIMIT_BYTES,
        FILES_COUNT,
        true,
    )

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        writeMessageToFile(tag, getLogLevel(priority), message)
    }

    private fun getLogLevel(priority: Int): Level =
        when (priority) {
            Log.ERROR -> Level.SEVERE
            Log.WARN -> Level.WARNING
            else -> Level.INFO
        }

    @SuppressLint("LogNotTimber")
    private fun writeMessageToFile(tag: String?, level: Level, message: String) {
        try {
            fileHandler.publish(LogRecord(level, message))
        } catch (e: IOException) {
            Log.e(tag, message, e) // Timber is not used because of StackOverflow error possibility
        }
    }

    companion object {

        const val LOG_FILE_NAME = BuildConfig.APPLICATION_ID

        private const val LOG_FILES_TEMPLATE = "$LOG_FILE_NAME%g.log"

        private const val SIZE_LIMIT_BYTES = 1024 * 504

        private const val FILES_COUNT = 2
    }
}
