package com.opasichnyi.beautify.di.module.navigation

import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class NavigationModule {

    @Provides @ActivityScoped
    fun provideOnBackPressedDispatcher(activity: FragmentActivity): OnBackPressedDispatcher =
        activity.onBackPressedDispatcher
}
