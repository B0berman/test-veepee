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
        } )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when {
            R.id.star.equals(item?.itemId) -> saveOrDeleteFromFavorites()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    fun saveOrDeleteFromFavorites() {

        val realm = Realm.getDefaultInstance()
        val resultMovie = realm.where(MovieDetail::class.java)
                .contains("title", movieDetail.title).findFirst()

        if (resultMovie == null) {
            addMoviewToFavorites()
        } else {
            removeMovieFromFavorites()
        }
        realm.close()
    }

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

    fun removeMovieFromFavorites() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm ->
            val result = realm.where(MovieDetail::class.java).equalTo("title", movieDetail.title).findAll()
            result.deleteAllFromRealm()
            realm.close()
        }
    }

    companion object {
        lateinit var queryProvider: QueryProvider
    }
}
