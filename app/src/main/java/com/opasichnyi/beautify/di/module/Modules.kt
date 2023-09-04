package com.opasichnyi.beautify.di.module

import com.opasichnyi.beautify.data.datasource.LoggedInUserDatasource
import com.opasichnyi.beautify.data.datasource.mock.MockAccountDataSource
import com.opasichnyi.beautify.data.datasource.mock.MockAppointmentsDataSource
import com.opasichnyi.beautify.data.repository.impl.AppointmentsRepositoryImpl
import com.opasichnyi.beautify.data.repository.impl.UserRepositoryImpl
import com.opasichnyi.beautify.domain.interactor.GetUpcomingAppointmentsInteractor
import com.opasichnyi.beautify.domain.interactor.LoggedInUserInteractor
import com.opasichnyi.beautify.domain.interactor.LoginInteractor
import com.opasichnyi.beautify.domain.interactor.RegistrationInteractor
import com.opasichnyi.beautify.domain.repository.AppointmentsRepository
import com.opasichnyi.beautify.domain.repository.UserRepository
import com.opasichnyi.beautify.presentation.mapper.DomainAppointmentToUIAppointmentMapper
import com.opasichnyi.beautify.presentation.mapper.DomainRegistrationResultToUIMapper
import com.opasichnyi.beautify.presentation.mapper.UIRegisterDataToDomainMapper
import com.opasichnyi.beautify.presentation.viewmodel.AppointmentsViewModel
import com.opasichnyi.beautify.presentation.viewmodel.HomeActivityViewModel
import com.opasichnyi.beautify.presentation.viewmodel.HomeViewModel
import com.opasichnyi.beautify.presentation.viewmodel.LoginActivityViewModel
import com.opasichnyi.beautify.presentation.viewmodel.LoginViewModel
import com.opasichnyi.beautify.presentation.viewmodel.MainActivityViewModel
import com.opasichnyi.beautify.presentation.viewmodel.RegisterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val dataModule = module {

    single {
        LoggedInUserDatasource(
            context = androidContext()
        )
    }

    single {
        MockAccountDataSource(
            context = get(),
        )
    }

    single {
        MockAppointmentsDataSource(
            context = get(),
        )
    }

    single<UserRepository> {
        UserRepositoryImpl(
            loggedInUserDatasource = get(),
            accountDataSource = get()
        )
    }

    single<AppointmentsRepository> {
        AppointmentsRepositoryImpl(
            loggedInUserDatasource = get(),
            appointmentsDataSource = get(),
        )
    }
}

private val domainModule = module {

    single { LoggedInUserInteractor(userRepository = get()) }

    single { LoginInteractor(userRepository = get()) }

    single { RegistrationInteractor(userRepository = get()) }

    single { GetUpcomingAppointmentsInteractor(appointmentsRepository = get()) }
}

private val viewModelModule = module {

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

    viewModel {
        RegisterViewModel(
            registrationInteractor = get(),
            uiRegisterDataToDomainMapper = get(),
            domainRegistrationResultToUIMapper = get(),
        )
    }

    viewModel {
        AppointmentsViewModel(
            getUpcomingAppointmentsInteractor = get(),
            appointmentMapper = get()
        )
    }
}

private val presentationModule = module {
    single { UIRegisterDataToDomainMapper() }

    single { DomainRegistrationResultToUIMapper() }

    single { DomainAppointmentToUIAppointmentMapper() }
}

internal val appModules =
    listOf(
        dataModule,
        domainModule,
        presentationModule,
        viewModelModule,
    )
