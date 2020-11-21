package com.vp.detail

import android.os.AsyncTask
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import com.vp.database.async.doAsync
import com.vp.database.beans.Movie
import com.vp.database.db.MovieDatabase
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject
import kotlin.run

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var favoriteView: MenuItem
    lateinit var movie: Movie
    var isFavorite: MutableLiveData<Boolean> = MutableLiveData()

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
        isFavorite.observe(this, Observer {
            if (it)
                favoriteView.setIcon(R.drawable.ic_star_full)
            else
                favoriteView.setIcon(R.drawable.ic_star)
        })
        detailViewModel.details().observe(this, Observer { details ->
            movie = Movie(getMovieId(), details.title, details.poster)
            doAsync {
                val favoriteMovies = MovieDatabase.getDatabase(this).movieDao().allFavoriteMovies()
                favoriteMovies.find { it.id == this.getMovieId() }?.let {
                    isFavorite.postValue(true)
                } ?: run {
                    isFavorite.postValue(false)
                }
            }.execute()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        favoriteView = menu!!.findItem(R.id.star)
        favoriteView.setOnMenuItemClickListener {
            doAsync {
                if (isFavorite.value!!) {
                    isFavorite.postValue(false)
                    MovieDatabase.getDatabase(this).movieDao().remove(movie)
                } else {
                    isFavorite.postValue(true)
                    MovieDatabase.getDatabase(this).movieDao().insert(movie)
                }
            }.execute()
            true
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
