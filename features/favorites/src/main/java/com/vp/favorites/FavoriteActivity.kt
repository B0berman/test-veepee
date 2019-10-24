package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity() {

    private lateinit var listAdapter: ListAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var favoriteViewModel: FavoriteViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        initList()

        favoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)

        favoriteViewModel?.favoriteMovies()?.observe(this, Observer {
            listAdapter.movies = it
        })
    }

    override fun onStart() {
        super.onStart()
        favoriteViewModel?.fetchMovies()
    }

    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter.onItemClickListener = {
            openDetails(it.imdbID)
        }
        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            GRID_PORTRAIT_ORIENTATION_SPAN_COUNT
        } else GRID_LANDSCAPE_ORIENTATION_SPAN_COUNT

        val layoutManager = GridLayoutManager(this, spanCount)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = listAdapter
    }

    private fun openDetails(imdbID: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
        intent.setPackage(packageName)
        startActivity(intent)
    }

    companion object {
        private const val GRID_PORTRAIT_ORIENTATION_SPAN_COUNT = 2
        private const val GRID_LANDSCAPE_ORIENTATION_SPAN_COUNT = 3
    }
}
