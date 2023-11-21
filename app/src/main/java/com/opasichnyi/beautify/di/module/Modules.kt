package com.opasichnyi.beautify.di.module

import com.google.gson.GsonBuilder
import com.opasichnyi.beautify.data.datasource.LoggedInUserDatasource
import com.opasichnyi.beautify.data.datasource.remote.RemoteAccountDataSource
import com.opasichnyi.beautify.data.datasource.remote.RemoteAppointmentsDataSource
import com.opasichnyi.beautify.data.datasource.remote.service.AccountService
import com.opasichnyi.beautify.data.datasource.remote.service.AppointmentService
import com.opasichnyi.beautify.data.mapper.AppointmentCreationResultMapper
import com.opasichnyi.beautify.data.mapper.DataAppointmentToDomainMapper
import com.opasichnyi.beautify.data.mapper.DataRegisterDataToDomainMapper
import com.opasichnyi.beautify.data.mapper.DataUserAccountToDomainMapper
import com.opasichnyi.beautify.data.mapper.DataUserInfoToDomainMapper
import com.opasichnyi.beautify.data.mapper.RegisterResultMapper
import com.opasichnyi.beautify.data.repository.impl.AppointmentsRepositoryImpl
import com.opasichnyi.beautify.data.repository.impl.UserRepositoryImpl
import com.opasichnyi.beautify.domain.interactor.CompleteAppointmentInteractor
import com.opasichnyi.beautify.domain.interactor.DeleteAppointmentInteractor
import com.opasichnyi.beautify.domain.interactor.DeleteCurrentAccountInteractor
import com.opasichnyi.beautify.domain.interactor.EditAppointmentInteractor
import com.opasichnyi.beautify.domain.interactor.GetUpcomingAppointmentsInteractor
import com.opasichnyi.beautify.domain.interactor.GetUserInfoInteractor
import com.opasichnyi.beautify.domain.interactor.GetUsersInteractor
import com.opasichnyi.beautify.domain.interactor.LoggedInUserInteractor
import com.opasichnyi.beautify.domain.interactor.LoginInteractor
import com.opasichnyi.beautify.domain.interactor.LogoutInteractor
import com.opasichnyi.beautify.domain.interactor.RateAppointmentInteractor
import com.opasichnyi.beautify.domain.interactor.RegistrationInteractor
import com.opasichnyi.beautify.domain.interactor.TryCreateAppointmentInteractor
import com.opasichnyi.beautify.domain.mapper.DateMapper
import com.opasichnyi.beautify.domain.repository.AppointmentsRepository
import com.opasichnyi.beautify.domain.repository.UserRepository
import com.opasichnyi.beautify.presentation.mapper.DomainAppointmentToUIAppointmentMapper
import com.opasichnyi.beautify.presentation.mapper.DomainRegistrationResultToUIMapper
import com.opasichnyi.beautify.presentation.mapper.DomainUserInfoToUIUserInfoMapper
import com.opasichnyi.beautify.presentation.mapper.UIRegisterDataToDomainMapper
import com.opasichnyi.beautify.presentation.viewmodel.AppointmentDetailsViewModel
import com.opasichnyi.beautify.presentation.viewmodel.AppointmentsViewModel
import com.opasichnyi.beautify.presentation.viewmodel.CreateAppointmentViewModel
import com.opasichnyi.beautify.presentation.viewmodel.HomeViewModel
import com.opasichnyi.beautify.presentation.viewmodel.LoginActivityViewModel
import com.opasichnyi.beautify.presentation.viewmodel.LoginViewModel
import com.opasichnyi.beautify.presentation.viewmodel.MainActivityViewModel
import com.opasichnyi.beautify.presentation.viewmodel.RegisterViewModel
import com.opasichnyi.beautify.presentation.viewmodel.SettingsViewModel
import com.opasichnyi.beautify.presentation.viewmodel.UserDetailsViewModel
import com.opasichnyi.beautify.presentation.viewmodel.UsersListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException


// TODO("Refactor")
fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
    return try {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { _, _ -> true }
        builder
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}

private val dataModule = module {

    single {
        LoggedInUserDatasource(
            context = androidContext(),
        )
    }

    single<UserRepository> {
        UserRepositoryImpl(
            loggedInUserDatasource = get(),
            userInfoMapper = get(),
            accountDataSource = get(),
            userAccountMapper = get(),
            registerResultMapper = get(),
            registerDataToDomainMapper = get()
        )
    }

    single<AppointmentsRepository> {
        AppointmentsRepositoryImpl(
            loggedInUserDatasource = get(),
            appointmentMapper = get(),
            appointmentsDataSource = get(),
            appointmentCreationResultMapper = get()
        )
    }

    single { DataUserInfoToDomainMapper(dateMapper = get()) }

    single { DataAppointmentToDomainMapper(dateMapper = get(), userAccountMapper = get()) }

    single { DataUserAccountToDomainMapper() }

    single { DateMapper() }

    single { RegisterResultMapper(userAccountMapper = get()) }

    single { DataRegisterDataToDomainMapper() }

    single { AppointmentCreationResultMapper(appointmentMapper = get()) }

    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = getUnsafeOkHttpClient().addInterceptor(interceptor).build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl("https://192.168.1.65:5000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    single {
        val retrofit: Retrofit = get()

        retrofit.create(AppointmentService::class.java)
    }

    single {
        val retrofit: Retrofit = get()

        retrofit.create(AccountService::class.java)
    }

    single { RemoteAccountDataSource(accountService = get()) }

    single { RemoteAppointmentsDataSource(appointmentService = get()) }
}

private val domainModule = module {

    single { LoggedInUserInteractor(userRepository = get()) }

    single { LoginInteractor(userRepository = get()) }

    single { RegistrationInteractor(userRepository = get()) }

    single { GetUpcomingAppointmentsInteractor(appointmentsRepository = get()) }

    single { GetUsersInteractor(userRepository = get()) }

    single { GetUserInfoInteractor(userRepository = get()) }

    single { TryCreateAppointmentInteractor(appointmentsRepository = get()) }

    single { LogoutInteractor(userRepository = get()) }

    single { DeleteAppointmentInteractor(appointmentsRepository = get()) }

    single { DeleteCurrentAccountInteractor(userRepository = get()) }

    single { CompleteAppointmentInteractor(appointmentsRepository = get()) }

    single { RateAppointmentInteractor(appointmentsRepository = get()) }

    single { EditAppointmentInteractor(appointmentsRepository = get()) }
}

private val viewModelModule = module {

    viewModel {
        MainActivityViewModel(
            loggedInUserInteractor = get(),
        )
    }

    viewModel {
        HomeViewModel()
    }

    viewModel {
        LoginActivityViewModel()
    }

    viewModel {
        LoginViewModel(
            loginInteractor = get(),
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
            appointmentMapper = get(),
        )
    }

    viewModel {
        AppointmentDetailsViewModel(
            appointmentMapper = get(),
            deleteAppointmentInteractor = get(),
            completeAppointmentInteractor = get(),
            rateAppointmentInteractor = get(),
        )
    }

    viewModel {
        UsersListViewModel(getUsersInteractor = get())
    }

    viewModel {
        UserDetailsViewModel(getUserInfoInteractor = get(), userInfoMapper = get())
    }

    viewModel {
        CreateAppointmentViewModel(
            loggedInUserInteractor = get(),
            tryCreateAppointmentInteractor = get(),
            appointmentToUIAppointmentMapper = get(),
            editAppointmentInteractor = get(),
        )
    }

    viewModel {
        SettingsViewModel(logoutInteractor = get(), deleteCurrentAccountInteractor = get())
    }
}

private val presentationModule = module {
    single { UIRegisterDataToDomainMapper() }

    single { DomainRegistrationResultToUIMapper() }

    single { DomainAppointmentToUIAppointmentMapper(dateMapper = get()) }

    single { DomainUserInfoToUIUserInfoMapper() }
}

internal val appModules =
    listOf(
        dataModule,
        domainModule,
        presentationModule,
        viewModelModule,
    )
