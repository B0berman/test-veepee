package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.database.async.doAsync
import com.vp.database.beans.Movie
import com.vp.database.db.MovieDatabase
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity(), FavoriteListAdapter.OnItemClickListener {
    private var listAdapter = FavoriteListAdapter()
    private var movies : MutableLiveData<List<Movie>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        initList()

        movies.observe(this, Observer {
            listAdapter.setItems(it)
        })
    }

    private fun initList() {
        listAdapter.setOnItemClickListener(this)
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView.layoutManager = layoutManager
        refreshFavorites()
    }

    private fun refreshFavorites() {
        doAsync {
            val favoriteMovies = MovieDatabase.getDatabase(this).movieDao().allFavoriteMovies()
            movies.postValue(favoriteMovies)
        }.execute()
    }

    override fun onItemClick(imdbID: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        refreshFavorites()
    }
}