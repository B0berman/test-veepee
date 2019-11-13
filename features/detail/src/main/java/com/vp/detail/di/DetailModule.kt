package com.vp.detail.di

import android.app.Activity
import com.vp.detail.DetailActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailModule {

    @ContributesAndroidInjector(modules = [DetailActivityModule::class])
    internal abstract fun bindDetailActivity(): DetailActivity

    @Module(includes = [DetailViewModelsModule::class])
    abstract class DetailActivityModule {

        @Binds
        internal abstract fun activity(activity: DetailActivity): Activity
    }
}