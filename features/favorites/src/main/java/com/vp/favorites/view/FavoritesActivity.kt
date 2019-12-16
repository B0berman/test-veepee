package com.vp.favorites.view;

import android.content.Intent
import android.os.Bundle;
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.muhammedsafiulazam.photoalbum.feature.albumlist.listener.IFavoritesListener
import com.vp.detail.DetailActivity
import com.vp.favorites.R
import com.vp.favorites.databinding.ActivityFavoritesBinding
import com.vp.favorites.viewmodel.FavoritesActivityModel
import com.vp.movies.db.Movie
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorites.*
import javax.inject.Inject


class FavoritesActivity : DaggerAppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, IFavoritesListener {

    @Inject
    lateinit var mFactory: ViewModelProvider.Factory

    // ViewModel.
    lateinit var mViewModel: FavoritesActivityModel

    // Flags.
    private var isViewReady: Boolean = false

    private val mMovies: MutableList<Movie> = mutableListOf()
    private val mMoviesAdapter: FavoritesAdapter by lazy {
        FavoritesAdapter(mMovies, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityFavoritesBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorites)
        mViewModel = ViewModelProviders.of(this, mFactory).get(FavoritesActivityModel::class.java)
        binding.viewModel = mViewModel
        binding.setLifecycleOwner(this)

        val gridLayoutManager = androidx.recyclerview.widget.GridLayoutManager(this,
                FavoritesAdapter.SPAN_SIZE
        )
        favorites_ryv_movies.setLayoutManager(gridLayoutManager)
        favorites_ryv_movies.adapter = mMoviesAdapter

        favorties_srl_movies.setOnRefreshListener(this)

        loadMovies()
    }

    override fun onStart() {
        super.onStart()

        if (!isViewReady) {
            isViewReady = true
            mViewModel.onCreate()
        }
    }

    fun loadMovies() {

        favorites_pgb_movies.visibility = View.VISIBLE
        favorites_txv_message.visibility = View.GONE

        mViewModel.loadMovies(callback = { movies: List<Movie>, error: Throwable? -> Unit

            favorites_pgb_movies.visibility = View.GONE
            favorites_txv_message.visibility = View.GONE

            if (movies.isNullOrEmpty()) {
                mMovies.clear()
                mMoviesAdapter.notifyDataSetChanged()
                favorites_txv_message.visibility = View.VISIBLE
            } else {
                mMovies.clear()
                mMovies.addAll(movies)
                mMoviesAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onRefresh() {
        favorties_srl_movies.isRefreshing = false
        loadMovies()
    }

    override fun onClickMovie(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("imdbID", movie.id)
        startActivity(intent)
    }

    override fun onDestroy() {
        mViewModel.onDestroy()
        super.onDestroy()
    }
}