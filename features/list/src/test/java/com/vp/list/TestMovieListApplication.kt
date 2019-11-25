package com.vp.list

import android.app.Activity
import android.app.Application
import com.vp.daggeraddons.global.module.NetworkModule
import com.vp.list.di.DaggerTestAppComponent
import com.vp.list.di.TestAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class TestMovieListApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector : DispatchingAndroidInjector<Activity>

    lateinit var testAppComponent : TestAppComponent


    override fun activityInjector(): AndroidInjector<Activity> {

        return dispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()

       testAppComponent = DaggerTestAppComponent.builder().application(this)
                // Could be set in global Dagger too, a matter of preference/design - CE
                .setNetworkModule(NetworkModule(BuildConfig.API_KEY))
                .build()

        testAppComponent.inject(this)

        setTheme(R.style.Theme_AppCompat)
    }

}