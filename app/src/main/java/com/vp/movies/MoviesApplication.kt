package com.vp.movies

import android.app.Activity
import android.app.Application
import com.vp.movies.di.DaggerAppComponent
import com.vp.storage.DataBaseManager
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MoviesApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

   @Inject
    lateinit var databaseManger: DataBaseManager

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

        databaseManger.initDataBase(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? = dispatchingActivityInjector
}
