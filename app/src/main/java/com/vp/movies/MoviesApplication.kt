package com.vp.movies

import android.app.Activity
import android.app.Application
import com.vp.daggeraddons.global.module.NetworkModule
import com.vp.movies.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MoviesApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                // Could be set in global Dagger too, a matter of preference/design - CE
                .networkModule(NetworkModule(BuildConfig.API_KEY))
                .build()
                .inject(this)

    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? = dispatchingActivityInjector
}
