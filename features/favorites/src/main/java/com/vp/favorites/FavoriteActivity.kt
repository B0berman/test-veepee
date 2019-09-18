package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

/**
 * Created by Uxio Lorenzo on 2019-09-10.
 */
class FavoriteActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var favoriteViewModel: FavoriteViewModel

    private val adapter: ListAdapter by lazy {
        ListAdapter(::onItemClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        favoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)

        recyclerViewFavorites.layoutManager = GridLayoutManager(this,
            if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerViewFavorites.adapter = adapter

        favoriteViewModel.favoriteMovie.observe(this, Observer {
            tvNoResults.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE

            adapter.onItems(it)
        })

        favoriteViewModel.loadingState.observe(this, Observer {
            when(it) {
                FavoriteViewModel.LoadingState.IN_PROGRESS -> progressBar.visibility = View.VISIBLE
                else -> progressBar.visibility = View.GONE
            }
        })

    }

    override fun onResume() {
        super.onResume()

        // retrieve onResume to check if favorites has change after navigating to movie detail
        favoriteViewModel.getFavorites()
    }


    fun onItemClicked(omdbId: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$omdbId"))
        intent.setPackage(packageName)
        startActivity(intent)
    }

}