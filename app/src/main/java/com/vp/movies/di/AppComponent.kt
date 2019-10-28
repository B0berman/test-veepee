package com.vp.movies.di

import com.vp.detail.di.DetailActivityModule
import com.vp.favorites.di.FavoriteActivityModule
import com.vp.favorites.di.FrameworkModule
import com.vp.list.di.MovieListActivityModule
import com.vp.movies.MoviesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        FrameworkModule::class,
        MovieListActivityModule::class,
        DetailActivityModule::class,
        FavoriteActivityModule::class]
)

interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: MoviesApplication): Builder

        fun build(): AppComponent
    }

}