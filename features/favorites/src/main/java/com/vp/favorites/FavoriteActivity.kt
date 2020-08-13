package com.vp.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoriteAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FavoriteAdapter()
        adapter.onItemClickListener = object : FavoriteAdapter.OnItemClickListener {
            override fun onItemClick(imdbID: String?) {
                val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.Builder()
                                .scheme("app")
                                .authority("movies")
                                .appendPath("details")
                                .appendQueryParameter("imdbID", imdbID)
                                .build()
                )
                intent.setPackage(packageName)
                startActivity(intent)
            }
        }
        recyclerView.adapter = adapter

        val favouriteViewModel = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)

        favouriteViewModel.favorites().observe(this, Observer { favoriteItems ->
            adapter.items = favoriteItems
            adapter.notifyDataSetChanged()
        })
    }
}
