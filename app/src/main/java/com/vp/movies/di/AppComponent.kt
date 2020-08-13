package com.vp.movies.di

import android.app.Application
import android.content.Context
import com.vp.detail.di.DetailActivityModule
import com.vp.detail.di.DetailLocalDataSourceModule
import com.vp.favorites.di.FavoritesActivityModule
import com.vp.favorites.di.FavoritesLocalDataSourceModule
import com.vp.list.di.MovieListActivityModule
import com.vp.movies.MoviesApplication
import com.vp.storage.di.StorageModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            NetworkModule::class,
            StorageModule::class,

            MovieListActivityModule::class,

            FavoritesActivityModule::class,
            FavoritesLocalDataSourceModule::class,

            DetailActivityModule::class,
            DetailLocalDataSourceModule::class
        ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(app: MoviesApplication)
}
