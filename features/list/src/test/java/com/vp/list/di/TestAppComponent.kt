package com.vp.list.di

import android.app.Application
import com.vp.daggeraddons.global.module.NetworkModule
import com.vp.list.BaseTest
import com.vp.list.TestMovieListApplication
import com.vp.list.di.module.TestListNetworkModule
import com.vp.list.di.module.TestMovieListActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, TestMovieListActivityModule::class, NetworkModule::class, TestListNetworkModule::class])
interface TestAppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application : Application) : Builder
        fun setNetworkModule(networkModule: NetworkModule) : Builder
        fun build() : TestAppComponent
    }

    fun inject(testMovieListApplication: TestMovieListApplication)

    fun inject(baseTest : BaseTest)

}