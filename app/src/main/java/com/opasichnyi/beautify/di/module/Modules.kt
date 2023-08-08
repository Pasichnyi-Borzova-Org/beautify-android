package com.opasichnyi.beautify.di.module

import com.opasichnyi.beautify.presentation.base.BaseViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val dataModule = module {
    // define all Repos as single
}

private val domainModule = module {
    // define all interactors as single
}

private val presentationModule = module {

    // define all viewmodels in format
    //     viewModel {
    //        MyViewModel(
    //            interactor1 = get(),
    //            service1 = get(),
    //        )
    //    }

}

internal val appModules =
    listOf(
        presentationModule,
    )