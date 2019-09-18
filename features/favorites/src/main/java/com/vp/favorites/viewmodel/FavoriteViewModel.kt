package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.persistance.BACKGROUND
import com.vp.persistance.RepositoryDB
import com.vp.persistance.model.FavoriteMovie
import javax.inject.Inject


class FavoriteViewModel @Inject constructor(
    private val repositoryDB: RepositoryDB
) : ViewModel() {

    private val _loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    val loadingState: LiveData<LoadingState> = _loadingState

    private val _favoriteMovies: MutableLiveData<List<FavoriteMovie>> = MutableLiveData()
    val favoriteMovie: LiveData<List<FavoriteMovie>> = _favoriteMovies


    fun getFavorites() {
        BACKGROUND.submit {
            _loadingState.postValue(LoadingState.IN_PROGRESS)
            val result = repositoryDB.getAll()
            _loadingState.postValue(LoadingState.LOADED)
            _favoriteMovies.postValue(result)
        }
    }

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }

}