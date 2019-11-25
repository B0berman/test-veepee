package com.vp.favorites.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vp.favorites.R
import com.vp.favorites.core.model.room.Movie
import kotlinx.android.synthetic.main.item_list_movie.view.*

class FavoriteMovieAdapter(private var movieList : List<Movie> = listOf()) : RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {

    private var itemClickListener : ((movie : Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {

        return FavoriteMovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_movie, parent, false))
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        bindMovie(holder, movieList[holder.adapterPosition])
    }

    private fun bindMovie(holder: FavoriteMovieViewHolder, movie : Movie) {
        Picasso.get().load(movie.poster).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(holder.imageViewPoster)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(movie)
        }
    }

    fun setOnMovieItemClickListener(itemClickListener : (movie : Movie) -> Unit) {
        this.itemClickListener = itemClickListener
    }

    fun setMovieList(movieList : List<Movie>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    inner class FavoriteMovieViewHolder(itemViewParam : View) : RecyclerView.ViewHolder(itemViewParam) {
        val imageViewPoster: ImageView = itemViewParam.poster
    }
}