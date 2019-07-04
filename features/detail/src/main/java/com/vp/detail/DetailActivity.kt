package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var dependencies: DetailsViewModel.Dependencies

    private val viewModel by lazy {
        ViewModelProviders
                .of(this, DetailsViewModel.Factory(movieId, dependencies))
                .get(DetailsViewModel::class.java)
    }

    private val movieId: String by lazy {
        intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    private var favoriteItem: MenuItem? = null
    private var favoriteObserver: Observer<Boolean> = Observer { isFavorite ->
        isFavorite?.run(::setFavoriteIcon)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        viewModel.fetchDetails()
        viewModel.title().observe(this, Observer {
            supportActionBar?.title = it
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        favoriteItem = menu?.findItem(R.id.star)

        with(viewModel.isFavorite()) {
            removeObserver(favoriteObserver) // In case of onCreateOptionsMenu called multiple times
            observe(this@DetailActivity, favoriteObserver)
            value?.run(::setFavoriteIcon)
        }
        return true
    }

    private fun setFavoriteIcon(isFavorite: Boolean) {
        favoriteItem?.icon = when (isFavorite) {
            true -> resources.getDrawable(R.drawable.ic_star_full, theme)
            false -> resources.getDrawable(R.drawable.ic_star_empty, theme)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.star) viewModel.switchFavorite()
        return super.onOptionsItemSelected(item)
    }
}
