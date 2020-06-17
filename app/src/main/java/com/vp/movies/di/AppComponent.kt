package com.vp.movies.di

import android.app.Application
import com.vp.detail.di.DetailActivityModule
import com.vp.favorites.FavoriteListActivity
import com.vp.favorites.FavoritesListFragment
import com.vp.favorites.di.FavoritesListActivityModule
import com.vp.favorites.di.FavoritesListFragmentModule
import com.vp.list.di.MovieListActivityModule
import com.vp.movies.MoviesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    RoomModule::class,
    NetworkModule::class,
    MovieListActivityModule::class,
    DetailActivityModule::class,
    FavoritesListActivityModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: MoviesApplication)

    fun application(): Application


}