package com.opasichnyi.beautify.di.module

import com.opasichnyi.beautify.util.extension.concurrent.safeCoroutineExceptionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ErrorHandlerModule {

    @Provides @Singleton
    fun provideSafeCoroutineExceptionHandler(): CoroutineExceptionHandler =
        safeCoroutineExceptionHandler
}
