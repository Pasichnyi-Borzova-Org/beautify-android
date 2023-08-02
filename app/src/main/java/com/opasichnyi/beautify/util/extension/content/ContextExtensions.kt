package com.opasichnyi.beautify.util.extension.content

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import android.view.LayoutInflater
import android.view.autofill.AutofillManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CheckResult
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import com.opasichnyi.beautify.util.extension.resources.dpToPx
import com.opasichnyi.beautify.util.extension.resources.pxToDp
import com.opasichnyi.beautify.util.resource.Dp

/**
 * @see LayoutInflater.from
 *
 * @return [LayoutInflater]
 */
@get:CheckResult inline val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

/**
 * @return [AutofillManager]
 */
@get:CheckResult
@get:RequiresApi(Build.VERSION_CODES.O)
inline val Context.autofillManager: AutofillManager?
    get() = applicationContext.getSystemService(AutofillManager::class.java)

/**
 * @see Context.VIBRATOR_SERVICE
 *
 * @return [Vibrator]
 * WARNING: The vibrator may not be available on cheap devices.
 */
@get:CheckResult inline val Context.vibrator: Vibrator?
    get() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSystemService(VibratorManager::class.java)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        }

/**
 * @see Context.INPUT_METHOD_SERVICE
 *
 * @return [InputMethodManager]
 */
@get:CheckResult inline val Context.inputMethodManager
    get() = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

/**
 * @see Context.CONNECTIVITY_SERVICE
 *
 * @return [ConnectivityManager]
 */
@get:CheckResult val Context.connectivityManager: ConnectivityManager?
    get() = applicationContext
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

/**
 * @see PackageManager.queryIntentActivities
 * @see PackageManager.MATCH_DEFAULT_ONLY
 */
// TODO implement extension when context receivers will be stable
@SuppressLint("QueryPermissionsNeeded")
@CheckResult
fun Context.isIntentSafe(
    intent: Intent?,
    flag: Int = PackageManager.MATCH_DEFAULT_ONLY,
): Boolean =
    intent?.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val flags = PackageManager.ResolveInfoFlags.of(flag.toLong())
            packageManager.queryIntentActivities(this, flags)
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(this, flag)
        }.isNotEmpty()
    } ?: false

@Px @CheckResult
fun Context.dpToPx(@Dp dp: Float): Int = resources.dpToPx(dp)

@Dp @CheckResult
fun Context.pxToDp(@Px px: Int): Float = resources.pxToDp(px)
