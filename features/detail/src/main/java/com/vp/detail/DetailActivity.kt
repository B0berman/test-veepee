package com.vp.detail

import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.model.Events
import com.vp.detail.model.FavoriteInfo
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel : DetailsViewModel

    private var startMenuItem : MenuItem? = null

    companion object {
        const val MOVIE_ID_KEY_EXTRA : String = "imdbID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        binding.setLifecycleOwner(this)

        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })

        detailViewModel.isMovieInFavorite.observe(this, Observer {
            refreshFavoriteState(it)
        })

    }

    override fun onStart() {
        super.onStart()
        detailViewModel.sendEvent(Events.GetMovieDetails(getMovieId()))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)

        startMenuItem = menu?.findItem(R.id.star)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId) {
            R.id.star -> {
                if(detailViewModel.details.value == null) {
                    Toast.makeText(this, "Unable to add this movie to favorite", Toast.LENGTH_LONG).show()
                    return true
                }

                detailViewModel.sendEvent(Events.AddOrRemoveFavorite(detailViewModel.details.value!!, detailViewModel.isMovieInFavorite.value != null && detailViewModel.isMovieInFavorite.value!!.isInFavorite))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshFavoriteState(favoriteInfo : FavoriteInfo) {
        if(favoriteInfo.isInFavorite) {
            startMenuItem?.icon?.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN)
            if(favoriteInfo.isNewlyAdded)
              Toast.makeText(this, "Added To Favorite", Toast.LENGTH_LONG).show()
        }
        else {
            startMenuItem?.icon?.clearColorFilter()
        }
    }

    private fun getMovieId(): String {

        return intent.data.getQueryParameter(MOVIE_ID_KEY_EXTRA) ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

}
