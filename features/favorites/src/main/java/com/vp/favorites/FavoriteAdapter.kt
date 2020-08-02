package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.favorites.model.FavoriteMovie
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteAdapter(private val listener: OnItemClickListener): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var favoriteItems: List<FavoriteMovie> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false))
    }

    override fun getItemCount() = favoriteItems.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteItems[position], listener)
    }

    fun setItems(favoriteItems: List<FavoriteMovie>) {
        this.favoriteItems = favoriteItems
        notifyDataSetChanged()
    }

    class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.favoriteMovieCoverImageView)

        fun bind(favoriteMovie: FavoriteMovie, listener: OnItemClickListener) {
            with(image) {
                Glide.with(this)
                        .load(favoriteMovie.cover)
                        .into(this)
            }
            itemView.favoriteMovieTitleTextView.text = favoriteMovie.title
            itemView.setOnClickListener { listener.onItemClick(favoriteMovie.movieId) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }
}