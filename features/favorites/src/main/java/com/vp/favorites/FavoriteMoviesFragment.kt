package com.vp.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.viewmodel.FavoriteMovieViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class FavoriteMoviesFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val favoriteMovieRecyclerViewAdapter = FavoriteMovieRecyclerViewAdapter()
    private lateinit var viewModel:FavoriteMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(FavoriteMovieViewModel::class.java)
        viewModel.requestFavorites()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_movie_list,
                container,
                false)

        // Set the favoriteMovieRecyclerViewAdapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager =  LinearLayoutManager(context)
                this.adapter = favoriteMovieRecyclerViewAdapter
            }
        }

        viewModel.favorites().observe(this, Observer {
            favoriteMovieRecyclerViewAdapter.setItems(it)
        })

        return view
    }

}
