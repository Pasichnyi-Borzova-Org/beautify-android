package com.opasichnyi.beautify.di.module

import com.opasichnyi.beautify.data.datasource.LoggedInUserDatasource
import com.opasichnyi.beautify.data.datasource.mock.MockAccountDataSource
import com.opasichnyi.beautify.data.mapper.DataUserToDomainMapper
import com.opasichnyi.beautify.data.repository.iface.UserRepository
import com.opasichnyi.beautify.data.repository.impl.UserRepositoryImpl
import com.opasichnyi.beautify.domain.interactor.LoggedInUserInteractor
import com.opasichnyi.beautify.domain.interactor.LoginInteractor
import com.opasichnyi.beautify.presentation.viewmodel.HomeActivityViewModel
import com.opasichnyi.beautify.presentation.viewmodel.HomeViewModel
import com.opasichnyi.beautify.presentation.viewmodel.LoginActivityViewModel
import com.opasichnyi.beautify.presentation.viewmodel.LoginViewModel
import com.opasichnyi.beautify.presentation.viewmodel.MainActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val dataModule = module {

    single {
        LoggedInUserDatasource(
            context = androidContext()
        )
    }

    single { DataUserToDomainMapper() }

    single { MockAccountDataSource() }

    single<UserRepository> {
        UserRepositoryImpl(
            loggedInUserDatasource = get(),
            dataUserToDomainMapper = get(),
            accountDataSource = get()
        )
    }
}

private val domainModule = module {

    single { LoggedInUserInteractor(userRepository = get()) }

    single { LoginInteractor(userRepository = get()) }
}

private val presentationModule = module {

    viewModel {
        MainActivityViewModel(
            loggedInUserInteractor = get()
        )
    }

    viewModel {
        HomeActivityViewModel()
    }

    viewModel {
        LoginActivityViewModel()
    }

    viewModel {
        LoginViewModel(
            loginInteractor = get()
        )
    }

    viewModel {
        HomeViewModel(
            repository = get()
        )
    }
}

internal val appModules =
    listOf(
        dataModule,
        domainModule,
        presentationModule,
    )