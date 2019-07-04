package com.vp.favorites.di

import android.content.Context
import com.vp.core.favorites.FavoriteMoviesRepository
import com.vp.favorites.persistence.FavoriteMoviesRoomRepository
import com.vp.favorites.persistence.room.FavoritesDao
import com.vp.favorites.persistence.room.FavoritesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FavoritePersistenceModule {

    @Singleton
    @Provides
    internal fun provideFavoritesDatabase(context: Context): FavoritesDatabase {
        return FavoritesDatabase.create(context)
    }

    @Provides
    internal fun provideFavoritesDao(db: FavoritesDatabase): FavoritesDao {
        return db.getDao()
    }

    @Provides
    internal fun provideFavoriteMoviesRepository(repository: FavoriteMoviesRoomRepository): FavoriteMoviesRepository {
        return repository
    }
}