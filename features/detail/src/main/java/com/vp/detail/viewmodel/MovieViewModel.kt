/*
 * Created by Alexis Rodriguez Paret on 3/12/20 9:17 PM
 * Copyright (c) 2020. All rights reserved. Heliocor (alexis.rodriquez@heliocor.com)
 * Last modified 3/12/20 9:17 PM
 *
 */

package com.vp.detail.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vp.persistance.db.MovieRepository
import com.vp.persistance.db.MovieRoomDatabase
import com.vp.persistance.model.MovieModel
import kotlinx.coroutines.launch

/**
 * Created by Alexis Rodríguez Paret on 2020-03-12.
 */
class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository

    val allMovies: LiveData<List<MovieModel>>

    val movie: MutableLiveData<MovieModel> = MutableLiveData()

    val exist: MutableLiveData<Boolean> = MutableLiveData()

    init {
        val wordsDao = MovieRoomDatabase.getDatabase(application, viewModelScope).movieDao()
        repository = MovieRepository(wordsDao)
        allMovies = repository.allMovies
    }

    fun observeMovies(): LiveData<MovieModel> {
        return movie
    }

    fun getById(id: String): LiveData<MovieModel> {
        return repository.observeMovieByID(id)
    }

    fun insert(movieToInsert: MovieModel) = viewModelScope.launch {
        repository.insert(movieToInsert)
    }

    fun removeItem(id: String) = viewModelScope.launch {
        repository.remove(id)
    }

}