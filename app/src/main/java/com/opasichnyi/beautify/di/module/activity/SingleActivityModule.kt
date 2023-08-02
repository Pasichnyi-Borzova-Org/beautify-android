package com.opasichnyi.beautify.di.module.activity

import com.opasichnyi.beautify.presentation.base.observable.ReadRootDataObservable
import com.opasichnyi.beautify.presentation.base.observable.RootDataObservable
import com.opasichnyi.beautify.presentation.base.observable.WriteRootDataObservable
import com.opasichnyi.beautify.presentation.impl.observable.RootDataObservableImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
interface SingleActivityModule {

    @Binds @ActivityRetainedScoped
    fun bindWriteRootDataObservable(observable: RootDataObservableImpl): WriteRootDataObservable

    @Binds @ActivityRetainedScoped
    fun bindReadRootDataObservable(observable: WriteRootDataObservable): ReadRootDataObservable

    @Binds @ActivityRetainedScoped
    fun bindRootDataObservable(observable: ReadRootDataObservable): RootDataObservable
}
