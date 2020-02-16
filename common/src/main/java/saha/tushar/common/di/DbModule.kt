package saha.tushar.common.di

import android.app.Application
import android.content.Context
import com.tushar.todosample.db.FavMovieDb
import dagger.Module
import dagger.Provides
import saha.tushar.common.db.FavMovieRepository

@Module
class DbModule {
    @Provides
    fun providesFavouriteMovieDb(application: Application): FavMovieDb = FavMovieDb.getDatabase(application)
}