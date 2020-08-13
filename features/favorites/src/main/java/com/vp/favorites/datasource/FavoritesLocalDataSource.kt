package com.vp.favorites.datasource

import androidx.lifecycle.LiveData
import com.vp.favorites.model.FavouriteItem

interface FavoritesLocalDataSource {
    fun favorites(): LiveData<List<FavouriteItem>>
}
