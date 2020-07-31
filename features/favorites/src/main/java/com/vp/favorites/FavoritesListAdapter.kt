package com.vp.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.databinding.ItemFavoriteBinding
import com.vp.favorites.model.MovieFavorite


class FavoritesListAdapter(private inline val onFavoriteClick: (imdbID: String) -> Unit): RecyclerView.Adapter<FavoritesListAdapter.ViewHolder>() {

    private var favorites : List<MovieFavorite> = listOf()

    override fun getItemCount(): Int  = favorites.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemFavoriteBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite= favorites[position]
        holder.bind(favorite)
    }

    fun setItems(favorites: List<MovieFavorite>) {
        this.favorites = favorites
        notifyDataSetChanged()
    }

    fun onFavoriteClick (imdbID: String) = onFavoriteClick.invoke(imdbID)

    inner class ViewHolder(
            private val binding: ViewDataBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(favorite: MovieFavorite){
            binding.setVariable(BR.favorite,favorite)
            binding.setVariable(BR.adapter, this@FavoritesListAdapter)
            binding.executePendingBindings()
        }



    }


}