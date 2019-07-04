package com.vp.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.vp.favorites.viewmodel.FavoriteViewModel
import com.vp.favorites.viewmodel.model.FavoriteMovie
import com.vp.favorites.viewmodel.model.FavoriteMovieDeletion
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val adapter = FavoriteAdapter(this::onShow, this::onRemove)
                .apply { lifecycle.addObserver(this) }

        recyclerView.adapter = adapter
        viewModel.getMoviesList().observe(this, Observer { list ->
            list?.let { adapter.submitList(it) }
        })

        viewModel.getDeletions().observe(this, Observer { deletion ->
            deletion?.run(::showSnackbar)
        })
    }

    private fun showSnackbar(deletion: FavoriteMovieDeletion) {
        Snackbar
                .make(coordinatorLayout, R.string.removed_from_favorites_message, Snackbar.LENGTH_LONG)
                .setAction(R.string.removed_from_favorites_undo) { deletion.undo() }
                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        deletion.dismiss()
                    }
                })
                .show()
    }

    private fun onShow(movie: FavoriteMovie) {
        val uri = Uri.Builder()
                .scheme("app")
                .authority("movies")
                .appendPath("detail")
                .appendQueryParameter("imdbID", movie.id)
                .build()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun onRemove(movie: FavoriteMovie) {
        viewModel.removeFromFavorites(movie)
    }
}
