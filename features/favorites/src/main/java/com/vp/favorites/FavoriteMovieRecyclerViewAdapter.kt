package com.vp.favorites


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.favorites.model.FavoriteMovie
import kotlinx.android.synthetic.main.favorite.view.*

class FavoriteMovieRecyclerViewAdapter
    : RecyclerView.Adapter<FavoriteMovieRecyclerViewAdapter.ViewHolder>() {

    private val values = mutableListOf<FavoriteMovie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.title.text = item.title
        holder.director.text = item.director

        if (item.posterUrl != "N/A") {
            Glide
                    .with(holder.posterView)
                    .load(item.posterUrl)
                    .into(holder.posterView)
        } else {
            holder.posterView.setImageResource(R.drawable.ic_favorite_movie_poster_placeholder)
        }

        holder.view.tag = item
    }

    override fun getItemCount(): Int = values.size

    fun setItems(movies:List<FavoriteMovie>) {
        values.addAll(movies)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.title
        val director: TextView = view.director
        val posterView: ImageView = view.imageView

        override fun toString(): String {
            return super.toString() + view.resources.getString(R.string.favorite_item_description,title.text,director.text)
        }
    }
}
