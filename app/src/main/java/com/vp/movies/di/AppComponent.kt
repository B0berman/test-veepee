package com.vp.movies.di

import android.app.Application
import com.vp.detail.di.DetailModule
import com.vp.favorites.di.FavoriteListModule
import com.vp.list.di.MovieListModule
import com.vp.movies.MoviesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    LocalModule::class,
    NetworkModule::class,
    RepositoryModule::class,
    MovieListModule::class,
    DetailModule::class,
    FavoriteListModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: MoviesApplication)
}