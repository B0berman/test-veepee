package com.vp.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.model.MovieDetail
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import io.realm.Realm
import javax.inject.Inject
import kotlin.run

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var movieDetail: MovieDetail
    lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        val detailViewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java)
        binding.viewModel = detailViewModel
        queryProvider = this
        binding.setLifecycleOwner(this)
        detailViewModel.fetchDetails()
        detailViewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })
        detailViewModel.details().observe(this, Observer {
            movieDetail = it
            val resultMovie = this.findMovieInRealm()
            if (resultMovie != null) {
                menuItem.setIcon(R.drawable.ic_star_selected)
            }
        } )
    }

    private fun findMovieInRealm(): MovieDetail? {
        val realm = Realm.getDefaultInstance()
        val realmMovie =  realm.where(MovieDetail::class.java)
                .contains(TITLE, movieDetail.title).findFirst()
        realm.close()
        return realmMovie
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu?.findItem(R.id.star)!!
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when {
            R.id.star.equals(item?.itemId) -> saveOrDeleteFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter(IMDBID) ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    /**
     * Manage Movie state in Realm DB
     */
    private fun saveOrDeleteFromFavorites(item: MenuItem?) {
        val resultMovie = this.findMovieInRealm()
        if (resultMovie == null) {
            item?.setIcon(R.drawable.ic_star_selected)
            addMoviewToFavorites()
        } else {
            item?.setIcon(R.drawable.ic_star)
            removeMovieFromFavorites()
        }
    }

    /**
     * Store movie in Realm DB to de displayed in FavoritesActivity
     */
    fun addMoviewToFavorites() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()

        val movie = realm.createObject(MovieDetail::class.java)
        movie.title = movieDetail.title
        movie.director = movieDetail.director
        movie.plot = movieDetail.plot
        movie.poster = movieDetail.poster
        movie.runtime = movieDetail.runtime
        movie.year = movieDetail.year

        realm.commitTransaction()
        realm.close()
    }

    /**
     * Remove movie from Realm DB.
     */
    private fun removeMovieFromFavorites() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm ->
            val result = realm.where(MovieDetail::class.java).equalTo(TITLE, movieDetail.title).findAll()
            result.deleteAllFromRealm()
        }
        realm.close()
    }

    companion object {
        lateinit var queryProvider: QueryProvider
        private const val TITLE: String = "title"
        private const val IMDBID: String = "imdbID"
    }
}
