package com.opasichnyi.beautify.di.module.viewmodel

import com.opasichnyi.beautify.presentation.base.entity.PresentationDataDelegate
import com.opasichnyi.beautify.presentation.base.observable.adapter.RootDataObservableAdapter
import com.opasichnyi.beautify.presentation.impl.entity.PresentationDataEntity
import com.opasichnyi.beautify.presentation.impl.observable.RootDataObservableAdapterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface BaseViewModelModule {

    @Binds
    fun bindPresentationDataDelegate(entity: PresentationDataEntity): PresentationDataDelegate

    @Binds
    fun bindRootDataObservableAdapter(
        adapter: RootDataObservableAdapterImpl,
    ): RootDataObservableAdapter
}
