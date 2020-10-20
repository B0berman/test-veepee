package com.vp.favorite.di

import android.app.Application
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.vp.favorite.Database
import com.vp.favorite.FavoriteMovieRepository
import com.vp.favorite.db.DbFavoriteMovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [DbModule::class])
interface FavoriteMovieModule {
    @Binds
    fun favoriteMovieRepository(impl: DbFavoriteMovieRepository): FavoriteMovieRepository
}

@Module
class DbModule {
    @Provides
    fun provideDatabase(driver: SqlDriver) = Database(driver)

    @Provides
    fun provideMoviesQueries(database: Database) = database.movieQueries

    @Provides
    fun provideSqlDriver(app: Application): SqlDriver = AndroidSqliteDriver(
        schema = Database.Schema,
        context = app,
        name = SQL_FILE
    )

    companion object {
        private const val SQL_FILE = "appDatabase.sql"
    }
}
