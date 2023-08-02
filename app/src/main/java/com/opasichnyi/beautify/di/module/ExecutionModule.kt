package com.opasichnyi.beautify.di.module

import com.opasichnyi.beautify.di.qualifier.execution.Computation
import com.opasichnyi.beautify.di.qualifier.execution.Foreground
import com.opasichnyi.beautify.di.qualifier.execution.IO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Hilt module that provides coroutine dispatchers
 */
@Module
@InstallIn(SingletonComponent::class)
class ExecutionModule {

    /**
     * Provides [Default] dispatcher which is used to perform operations
     * that require the processing of something
     */
    @Provides @Computation
    fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default

    /**
     * Provides [IO] dispatcher that is used for waiting or blocking calls
     */
    @Provides @IO
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Provides [Main] dispatcher that is used to update UI
     */
    @Provides @Foreground
    fun provideForegroundDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
