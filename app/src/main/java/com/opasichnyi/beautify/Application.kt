package com.opasichnyi.beautify

import android.app.Application
import com.opasichnyi.beautify.di.module.appModules
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Application : Application() {

    private fun initDi() {
        startKoin {
            androidContext(this@Application)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(appModules)
        }
    }
}