/*
 * Created by Alexis Rodriguez Paret on 3/13/20 6:19 PM
 * Copyright (c) 2020. All rights reserved. Heliocor (alexis.rodriquez@heliocor.com)
 * Last modified 3/13/20 6:19 PM
 *
 */

package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.databinding.ActivityFavoriteBinding
import com.vp.favorites.viewmodel.FavoritesViewModel
import com.vp.persistance.model.MovieModel
import dagger.android.support.DaggerAppCompatActivity


class FavoriteActivity : DaggerAppCompatActivity(), ListAdapter.OnItemClickListener {

    lateinit var favoritesViewModel: FavoritesViewModel

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFavoriteBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorite)
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
        binding.lifecycleOwner = this
        recyclerView = findViewById(R.id.recyclerView)

        favoritesViewModel.allMovies.observe(this, Observer {
            initList(it)
        })

    }

    fun initList(list: List<MovieModel>) {
        recyclerView.adapter = ListAdapter(list)
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView.layoutManager = layoutManager
        (recyclerView.adapter as ListAdapter).setOnItemClickListener(this)

    }

    override fun onItemClick(imdbID: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView.adapter = null
    }
}
