package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.databases.model.FavoriteMovie
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : AppCompatActivity(), ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        initList()

        val favoriteMovieViewModel: FavoriteMovieViewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel::class.java)
        favoriteMovieViewModel.getListFavorite().observe(this, Observer {
            getFavoriteMovies(it)
        })
    }

    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter.setOnItemClickListener(this)
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        var spanCount = 3
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 2
        }
        val layoutManager = GridLayoutManager(this, spanCount)
        recyclerView.layoutManager = layoutManager
    }

    private fun showList() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(recyclerView)
    }

    private fun showEmptyView() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(textEmpty)
    }

    override fun onItemClick(imdbID: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
        intent.setPackage(packageName)
        startActivity(intent)
    }

    private fun getFavoriteMovies(favoriteMovies: List<FavoriteMovie>) {
        if (favoriteMovies.isEmpty()) {
            showEmptyView()
        } else {
            listAdapter.setItems(favoriteMovies)
            showList()
        }
    }
}