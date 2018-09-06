package com.vp.database

import dagger.Binds
import dagger.Module

@Module
abstract class DataBaseModule {
    @Binds
    abstract fun bindsFavoritesStoraage(roomFavoritesStorage: RoomFavoritesStorage): FavoriteStorage
}