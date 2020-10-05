package com.vp.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vp.favorites.databinding.ActivityFavoriteBinding
import com.vp.favorites.model.FavoriteMovie
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity(), FavoriteAdapter.OnItemClickListener {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var favoriteViewModel: FavoriteViewModel

    private val adapter = FavoriteAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFavoriteBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorite)
        binding.setLifecycleOwner(this)

        favoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)
        favoriteViewModel.fetch()

        binding.viewModel = favoriteViewModel

        favoritesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        favoritesRecyclerView.adapter = adapter

        favoriteViewModel.liveData.observe(this, Observer {
            adapter.list = it
        })
    }

    override fun onFavoriteMovieClick(movie: FavoriteMovie) {
        val uri = Uri.parse("app://movies/detail?imdbID=${movie.id}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(packageName)
        startActivity(intent)
    }
}
