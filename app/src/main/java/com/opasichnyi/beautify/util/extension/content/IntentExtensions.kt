package com.opasichnyi.beautify.util.extension.content

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import androidx.annotation.CheckResult
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import com.opasichnyi.beautify.util.extension.resources.colorPrimary
import com.opasichnyi.beautify.util.media.MimeType
import timber.log.Timber
import java.io.Serializable

/**
 * Create [Intent] for App which can calling.
 *
 * @return [Intent] for [Activity.startActivity].
 */
@CheckResult
fun String.createCallIntent() = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$this"))

/**
 * Create [Intent] for App Google Maps with direction between current place and another location
 *
 * @param zoom Default: 13
 *
 * @return [Intent] for [Activity.startActivity].
 */
@CheckResult fun Location.createMapWithDirectionIntent(zoom: Int = 13): Intent {
    val gmmIntentUri = Uri.parse(
        "https://maps.google.com/maps?saddr=Current " +
            "Location&daddr=$latitude,$longitude?z=$zoom&q=$latitude,$longitude",
    )
    return Intent(Intent.ACTION_VIEW, gmmIntentUri).setPackage("com.google.android.apps.maps")
}

/**
 * Create [Intent] for open App Gmail and prepare a message for sending.
 *
 * Warning: [Activity.createShareIntent]
 *
 * @param text Set the literal text data to be sent as part of the share or null if not need.
 *             This may be a styled CharSequence.
 * @param subject Set a subject heading for this share; useful for sharing via email.
 * @param fileUri Optional*: Uri from another app or from your app.
 *                If you using file from your app, use [java.io.File.toUri].
 *                [Intent.putExtra] [Intent.EXTRA_STREAM]
 * @param fileMimeType Optional*: Set the type of data being shared. Example: image/jpeg.
 * @param emailArr Set an array of email addresses as recipients of this share.
 *                 This replaces all current "to" recipients that have been set so far.
 *                 [Intent.putExtra] [Intent.EXTRA_EMAIL]
 * @return [Intent] for [Activity.startActivity] and check [Context.isIntentSafe].
 *         If [Context.isIntentSafe] returned false, this function return null.
 *         If you got null then use [Activity.createShareIntent].
 *
 */
@CheckResult fun Context.createGmailIntent(
    text: CharSequence?,
    subject: String?,
    fileUri: Uri? = null,
    fileMimeType: String = "*/*",
    vararg emailArr: String,
): Intent? =
    Intent(if (fileUri == null) Intent.ACTION_SENDTO else Intent.ACTION_SEND)
        .putExtra(Intent.EXTRA_EMAIL, emailArr)
        .putExtra(Intent.EXTRA_SUBJECT, subject)
        .putExtra(Intent.EXTRA_TEXT, text)
        .setPackage("com.google.android.gm")
        .also {
            it.putExtra(Intent.EXTRA_STREAM, fileUri ?: return@also Timber.d("fileUri is null"))
            it.type = fileMimeType
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        .takeIf(::isIntentSafe)

/**
 * Create [Intent] for a Sharing any content.
 *
 * You can using this function for send email for all application which supported this function.
 * But if you need supported only Gmail, please use [Context.createGmailIntent]
 *
 * @see ShareCompat.IntentBuilder
 *
 * @param chooserTitle Set the title that will be used for the activity chooser for this share.
 *                     [ShareCompat.IntentBuilder.setChooserTitle]
 * @param text Set the literal text data to be sent as part of the share or null if not need.
 *             This may be a styled CharSequence. [ShareCompat.IntentBuilder.setText]
 * @param subject Set a subject heading for this share; useful for sharing via email.
 *                [ShareCompat.IntentBuilder.setSubject]
 * @param fileUri Optional*: Uri from another app or from your app.
 *                If you using file from your app, use [java.io.File.toUri].
 *                [Intent.putExtra] [Intent.EXTRA_STREAM]
 * @param mimeType Optional*: Set the type of data being shared. Example: image/jpeg or text/plain.
 *                 Default [MimeType.PLAIN_TEXT] for [ShareCompat.IntentBuilder.setType]
 * @param emailArr Optional*: Set an array of email addresses as recipients of this share.
 *                 This replaces all current "to" recipients that have been set so far.
 *                 [Intent.putExtra] [Intent.EXTRA_EMAIL]
 * @return [Intent] for [Activity.startActivity].
 */
@CheckResult fun Activity.createShareIntent(
    chooserTitle: CharSequence,
    text: CharSequence? = null,
    subject: String? = null,
    fileUri: Uri? = null,
    mimeType: String = MimeType.PLAIN_TEXT,
    emailArr: Array<String>? = null,
): Intent =
    ShareCompat.IntentBuilder(this)
        .setType(mimeType)
        .setChooserTitle(chooserTitle)
        .setSubject(subject)
        .setText(text)
        .intent
        .also { it.putExtra(Intent.EXTRA_EMAIL, emailArr ?: return@also) }
        .also {
            it.putExtra(Intent.EXTRA_STREAM, fileUri ?: return@also Timber.d("fileUri is null"))
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

/**
 * Create [Intent] for guideline
 *
 * @param url path for parse [String.toUri] and for open site in another activity
 *
 * @return [Intent] for [Activity.startActivity].
 */
@CheckResult fun Context.createBrowserIntent(url: String) =
    CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(colorPrimary)
                .build(),
        )
        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
        .build()
        .intent
        .setData(url.toUri())

/**
 * @see [Context.isIntentSafe]
 */
// TODO implement extension when context receivers will be stable
@CheckResult fun Intent?.isIntentSafe(context: Context) = context.isIntentSafe(this)

@CheckResult
inline fun <reified T : Parcelable> Intent.getParcelableCompat(name: String?): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableExtra(name)
    }

@CheckResult
inline fun <reified T : Serializable> Intent.getSerializableCompat(name: String?): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(name) as T?
    }
