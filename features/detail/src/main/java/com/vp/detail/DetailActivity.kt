package com.vp.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val detailViewModel: DetailsViewModel by lazy {
        ViewModelProvider(this, factory).get(DetailsViewModel::class.java)
    }
    private var starMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.viewModel = detailViewModel
        binding.lifecycleOwner = this

        intent?.data?.getQueryParameter("imdbID")?.let {
            detailViewModel.setMovieId(it)
        }
        detailViewModel.movie.observe(this, Observer {
            supportActionBar?.title = it.title
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        starMenuItem = menu?.findItem(R.id.star)

        detailViewModel.favorite.observe(this, Observer {
            starMenuItem?.icon = when (it) {
                true -> ContextCompat.getDrawable(this, R.drawable.ic_star_selected)
                else -> ContextCompat.getDrawable(this, R.drawable.ic_star)
            }
            starMenuItem?.isEnabled = true
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.star -> {
                starMenuItem?.isEnabled = false
                detailViewModel.toggleFavorite()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
