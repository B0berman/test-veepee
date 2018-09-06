package com.vp.favorites

import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.vp.database.FavoriteStorage
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var favoriteStorage: FavoriteStorage

    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        adapter = FavoritesAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        favoriteStorage.getFavoriteMovies().observe(this, Observer {
            it?.run {
                adapter.setItems(this)
            }
        })

        adapter.setOnItemClickListener(object : FavoritesAdapter.OnItemClickListener {
            override fun onItemClick(movieId: String) {
                val intent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("app://movies/details")
                                .buildUpon()
                                .appendQueryParameter("imdbID", movieId)
                                .build())
                intent.setPackage(packageName)
                startActivity(intent)
            }
        })

    }
}
