package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.databinding.ActivityFavoriteBinding
import com.vp.favorites.model.FavouriteItem
import com.vp.favorites.viewmodel.FavouriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity(), ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val favouriteViewModel by lazy { ViewModelProviders.of(this, factory).get(FavouriteViewModel::class.java) }
    private lateinit var binding: ActivityFavoriteBinding
    private val listAdapter by lazy { ListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite)
        binding.viewModel = favouriteViewModel
        binding.lifecycleOwner = this
        initList()

        favouriteViewModel.favouriteMovies().observe(this, Observer {
            updateListView(it)
        })
        favouriteViewModel.loadFavouriteMovies()
    }

    private fun initList() {
        listAdapter.setOnItemClickListener(this)
        binding.recyclerView.adapter = listAdapter
        binding.recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(baseContext,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun updateListView(movies: List<FavouriteItem>?) {
        with(listAdapter) {
            clearItems()
            setItems(movies)
        }
    }

    override fun onItemClick(imdbID: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/details?imdbID=$imdbID"))
        intent.setPackage(packageName)
        startActivity(intent)
    }
}
