package com.vp.favorites.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.R
import com.vp.favorites.adapter.FavoriteMovieAdapter
import com.vp.favorites.core.model.Events
import com.vp.favorites.core.model.State
import com.vp.favorites.core.model.room.Movie
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.favorite_fragment.*
import javax.inject.Inject

class FavoriteFragment : DaggerFragment() {


    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var favoriteViewModel : FavoriteViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavoriteViewModel::class.java)

        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = FavoriteMovieAdapter().also {
                val detailsIntent = Intent()

                it.setOnMovieItemClickListener {

                detailsIntent.apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse("app://movies/detail/?" + "imdbID" + "=" + it.movieId)
                    setPackage(requireContext().packageName)

                    startActivity(this)
                 }
                }
            }
        }

        favoriteViewModel.state.observe(viewLifecycleOwner, Observer { handleState(it) })
        favoriteViewModel.movieList.observe(viewLifecycleOwner, Observer { handleMovieList(it) })

        favoriteViewModel.send(Events.refreshFavoriteMovieList)

    }

    private fun handleState(state : State) {
        when(state) {
            is State.Error -> Toast.makeText(requireContext(), getString(R.string.no_favorite_movie), Toast.LENGTH_LONG).show()
            is State.Success -> Toast.makeText(requireContext(), getString(R.string.favorite_movie_loaded), Toast.LENGTH_LONG).show()
        }
    }

    private fun handleMovieList(movieList : List<Movie>) {
        (recyclerView.adapter as FavoriteMovieAdapter).setMovieList(movieList)
    }

}