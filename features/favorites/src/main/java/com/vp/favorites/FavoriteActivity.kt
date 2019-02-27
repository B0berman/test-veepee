package com.vp.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.persistence.FavouritesDao
import com.vp.persistence.FavouritesDaoFile
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavoriteActivity : AppCompatActivity() {

    private val favoritesDao = FavouritesDaoFile(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val recycler: RecyclerView = findViewById(R.id.favList)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = ListAdapter(favoritesDao, this)

    }
}

class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title = view.movie_title
    val remove = view.movie_delete
}

class ListAdapter(private val favDao: FavouritesDao, private val context: Context)
    : RecyclerView.Adapter<MovieViewHolder>() {

    private val movieList = favDao.listFavourites().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.favorite_item, parent, false)
        )
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder?.title?.text = movieList[position].title
        holder?.remove.setOnClickListener {
            favDao.removeFavourite(movieList[position].imdbId)
            movieList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}
