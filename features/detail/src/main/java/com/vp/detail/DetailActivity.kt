package com.vp.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.model.MovieDetailRealm
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import io.realm.Realm
import javax.inject.Inject
import kotlin.run

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    var menu: Menu? = null
    var favoriteSelected: Boolean = false
    var detailViewModel: DetailsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.setLifecycleOwner(this)
        detailViewModel?.fetchDetails()
        detailViewModel?.title()?.observe(this, Observer {
            supportActionBar?.title = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)

        val realm = Realm.getDefaultInstance()
        val favoriteMovie = realm.where(MovieDetailRealm::class.java).equalTo("imdbId", getMovieId()).findAll()
        if (favoriteMovie.size > 0) {
            menu?.findItem(R.id.star)?.setIcon(ContextCompat.getDrawable(applicationContext, R.drawable.ic_star_selected))
            favoriteSelected = true
        }

        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == R.id.star) {
            if (favoriteSelected) {
                menu?.findItem(R.id.star)?.setIcon(ContextCompat.getDrawable(applicationContext, R.drawable.ic_star))
                favoriteSelected = false
                detailViewModel?.removeFavoriteMovie()
            }
            else {
                menu?.findItem(R.id.star)?.setIcon(ContextCompat.getDrawable(applicationContext, R.drawable.ic_star_selected))
                favoriteSelected = true
                detailViewModel?.storeFavoriteMovie()
            }
        }
        return true
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    companion object {
        lateinit var queryProvider: QueryProvider
    }
}
