package com.vp.favorites

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val favoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)

        favoriteViewModel.fetchMovies()
        favoriteViewModel.favoriteMovies().observe(this, Observer {

        })
    }
}
