package com.opasichnyi.beautify.util.extension.storage

import android.content.Context
import android.net.Uri
import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import com.opasichnyi.beautify.BuildConfig
import java.io.File
import java.io.InputStream

/**
 * Save the file by destination stream.
 * Example copy file: You can get [InputStream] from [File.inputStream].
 *
 * @see InputStream.use
 * @see InputStream.copyTo
 * @see java.io.OutputStream.use
 *
 * @param inputStream stream another file / uri / url
 *
 * @return Same [File]
 *
 * @throws SecurityException
 *         If a security manager exists and its [java.lang.SecurityManager#checkRead]
 *         method denies read access to the file
 */
@Suppress("RedundantSuspendModifier")
@WorkerThread
suspend fun File.save(inputStream: InputStream): File {
    inputStream.use { input ->
        this@save.outputStream().use { fileOut ->
            input.copyTo(fileOut)
        }
    }
    return this
}

/**
 * Convert File to Uri.
 *
 * @see [FileProvider.getUriForFile]
 *
 * @receiver file from your app from internal storage
 *
 * @param context any context.
 *
 * @return Uri to the file path
 *
 * @throws IllegalArgumentException When the given [File] is outside the paths supported
 * by the provider.
 */
@CheckResult
@WorkerThread
fun File?.toUri(context: Context): Uri? =
    this?.let { FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, it) }
