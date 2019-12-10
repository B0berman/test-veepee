package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.saneme87.roomdatabase.FavoriteDao
import com.vp.favorites.model.ListItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(private val favoriteDao: FavoriteDao) : ViewModel() {

    private val liveData: MutableLiveData<List<ListItem>> = MutableLiveData()

    fun favorites(): LiveData<List<ListItem>> = liveData

    fun fetchFavorites() {
        viewModelScope.launch {
            val favorites = favoriteDao.getAllFavorites()
            liveData.postValue(favorites.map { ListItem(it.id, it.poster) })
        }
    }

}