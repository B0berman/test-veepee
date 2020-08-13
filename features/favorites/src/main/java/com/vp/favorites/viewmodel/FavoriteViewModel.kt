package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.vp.favorites.datasource.FavoritesLocalDataSource
import com.vp.favorites.model.FavouriteItem
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
        dataSource: FavoritesLocalDataSource
) : ViewModel() {

    private val liveData = dataSource.favorites()

    fun favorites(): LiveData<List<FavouriteItem>> = liveData
}
