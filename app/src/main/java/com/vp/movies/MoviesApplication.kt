package com.vp.movies

import android.app.Activity
import android.app.Application
import com.vp.movies.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import org.conscrypt.Conscrypt
import java.security.Security
import javax.inject.Inject

open class MoviesApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        Security.insertProviderAt(Conscrypt.newProvider(), 1);
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? = dispatchingActivityInjector
}
