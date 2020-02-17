package com.vp.favorites

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.tushar.todosample.db.FavMovieDb
import com.vp.favorites.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var favMovieAdapter: FavMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel::class.java)
        favouriteViewModel.init(FavMovieDb.getDatabase(this))
        favouriteViewModel.getFavList().observe(this, Observer {
            it?.let {
                if (it.isEmpty()) {
                    showEmpty()
                } else {
                    showList()
                    favMovieAdapter.setItems(it)
                }
            } ?: showEmpty()
        })
        initRecyclerview()
        showProgressBar()
    }

    private fun initRecyclerview() {
        favMovieAdapter = FavMovieAdapter()
        recyclerView.adapter = favMovieAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView.layoutManager = layoutManager
    }

    private fun showProgressBar() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(recyclerView)
    }

    private fun showEmpty() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(emptyText)
    }
}