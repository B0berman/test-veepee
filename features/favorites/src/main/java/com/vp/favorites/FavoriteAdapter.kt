package com.vp.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.databinding.FavoriteItemBinding
import com.vp.favorites.model.FavoriteMovie

class FavoriteAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    var list: List<FavoriteMovie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteMovie = list[position]
        holder.bind(favoriteMovie)
        holder.itemView.setOnClickListener {
            listener.onFavoriteMovieClick(favoriteMovie)
        }
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)) {
        private val binding: FavoriteItemBinding? = DataBindingUtil.bind(itemView)

        fun bind(movie: FavoriteMovie) {
            binding?.movie = movie
        }
    }

    interface OnItemClickListener {
        fun onFavoriteMovieClick(movie: FavoriteMovie)
    }
}
