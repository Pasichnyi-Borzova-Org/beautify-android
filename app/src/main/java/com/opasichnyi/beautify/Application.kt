package com.opasichnyi.beautify

import android.app.Application
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.opasichnyi.beautify.util.logger.CrashReportingTree
import com.opasichnyi.beautify.util.logger.DebugTree
import com.opasichnyi.beautify.util.test.OpenForTesting
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@OpenForTesting @HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        initWebView()
    }

    private fun initWebView() {
        if (WebViewFeature.isFeatureSupported(WebViewFeature.START_SAFE_BROWSING)) {
            runCatching { WebViewCompat.startSafeBrowsing(this) {} }
                .onFailure { Timber.w("The device doesn't support WebView.") }
        }
    }

    private fun setupTimber() {
        Timber.plant(CrashReportingTree(), DebugTree())
        Timber.i("Application.onCreate")
    }
}
