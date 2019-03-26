package com.vp.movies.di

import android.app.Application
import com.vp.detail.di.DetailActivityModule
import com.vp.list.di.FavouriteActivityModule
import com.vp.list.di.MovieListActivityModule
import com.vp.list.di.MovieListActivityModule_BindMovieListActivity
import com.vp.movies.MoviesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,  MovieListActivityModule::class,NetworkModule::class, FavouriteActivityModule::class, DetailActivityModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: MoviesApplication)
}