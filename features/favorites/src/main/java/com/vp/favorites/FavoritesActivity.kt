package com.vp.favorites

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.vp.detail.model.MovieDetail

import java.util.ArrayList

import io.realm.Realm
import io.realm.RealmResults

class FavoritesActivity : AppCompatActivity(), FavoritesAdapter.OnItemClickListener {

    private var recyclerView: RecyclerView? = null
    private var tvEmptyMessage: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        recyclerView = findViewById(R.id.recyclerView)
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage)

        recyclerView!!.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView!!.layoutManager = layoutManager
    }

    /**
     * Get and display all saved favorites
     */
    private fun fetchFavorites() {
        val favoriteMovies = ArrayList<MovieDetail>()
        val realm = Realm.getDefaultInstance()
        val resultMovies = realm.where(MovieDetail::class.java).findAll()

        if (resultMovies.isEmpty()) {
            favoriteMovies.clear()
            showEmptyView()
        } else {
            favoriteMovies.addAll(realm.copyFromRealm(resultMovies))
            setFavorites(favoriteMovies)
        }

        realm.close()
    }

    /**
     * Set favorites list in adapter
     */
    private fun setFavorites(favoriteMovies: ArrayList<MovieDetail>) {
        recyclerView!!.visibility = View.VISIBLE
        val favoritesAdapter = FavoritesAdapter()
        favoritesAdapter.setOnItemClickListener(this)
        favoritesAdapter.setItems(favoriteMovies)
        recyclerView!!.adapter = favoritesAdapter
    }

    /**
     * Displays a message if no favorites found
     */
    private fun showEmptyView() {
        recyclerView!!.visibility = View.GONE
        tvEmptyMessage!!.visibility = View.VISIBLE
        tvEmptyMessage!!.text = resources.getString(R.string.no_data)
    }

    override fun onItemClick(imdbID: String) {
        makeDetailIntent(imdbID)
    }

    override fun onResume() {
        fetchFavorites()
        super.onResume()
    }

    /**
     * Displays the detail activity.
     * @param imdbID the ID of the movie to display.
     */
    private fun makeDetailIntent(imdbID: String) {
        val deeplink = resources.getString(R.string.detail_deeplink) + imdbID
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(deeplink)
        startActivity(intent)
    }
}
