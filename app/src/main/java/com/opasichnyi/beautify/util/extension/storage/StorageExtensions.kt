package com.opasichnyi.beautify.util.extension.storage

import android.os.StatFs
import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread
import java.io.File

/**
 * Bytes in KB
 */
const val KB = 1L * 1024

/**
 * Bytes in MB
 */
const val MB = KB * 1024

/**
 * Bytes in GB
 */
const val GB = MB * 1024

/**
 * Convert bytes to KB
 */
@get:CheckResult inline val Long.bytesToKb get() = this / KB.toDouble()

/**
 * Convert bytes to MB
 */
@get:CheckResult inline val Long.bytesToMb get() = this / MB.toDouble()

/**
 * Convert bytes to GB
 */
@get:CheckResult inline val Long.bytesToGb get() = this / GB.toDouble()

/**
 * Available space size in this storage.
 * You can use any folder or file path in physical drive or sd-card.
 *
 * @throws IllegalArgumentException if the file system access fails
 */
@get:CheckResult
@get:WorkerThread
inline val File.availableBytes
    get() = StatFs(absolutePath).let { it.availableBlocksLong * it.blockSizeLong }

/**
 * File size in bytes
 *
 * @throws SecurityException
 *         If a security manager exists and its [java.lang.SecurityManager#checkRead]
 *         method denies read access to the file
 */
@get:CheckResult
@get:WorkerThread
val File.sizeInBytes: Long
    get() = if (isFile) length() else listFiles()?.sumOf(File::sizeInBytes) ?: 0
