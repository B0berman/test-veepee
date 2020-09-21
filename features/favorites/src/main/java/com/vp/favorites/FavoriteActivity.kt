package com.vp.favorites

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.viewmodel.FavoriteMovieViewModel
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    lateinit var favoriteViewModel: FavoriteMovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel::class.java)

        if (favoriteViewModel.fetchFavoriteMovies(this@FavoriteActivity).isEmpty()) {
            noDataText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noDataText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.apply {
                recyclerView.adapter = FavoriteAdapter(favoriteViewModel.fetchFavoriteMovies(context))
                recyclerView.setHasFixedSize(true)
                val layoutManager = GridLayoutManager(context,
                        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
                recyclerView.layoutManager = layoutManager
            }
        }

    }
}