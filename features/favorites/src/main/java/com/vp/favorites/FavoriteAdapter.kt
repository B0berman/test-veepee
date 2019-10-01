package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.database.entity.FavoriteEntity


class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var listItems = listOf<FavoriteEntity>()

    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movieItem = listItems[position]
        if (movieItem.poster!!.isNotEmpty() && UNAVAILABLE_IMAGE != movieItem.poster) {
            Glide
                    .with(holder.image)
                    .load(movieItem.poster)
                    .into(holder.image)
            holder.title.text = movieItem.title
        } else {
            holder.image.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(movieItems: List<FavoriteEntity>) {
        this.listItems = movieItems
        notifyDataSetChanged()
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        }
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView
        var title: TextView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
            title = itemView.findViewById(R.id.title)
        }

        override fun onClick(v: View) {
            onItemClickListener.onItemClick(listItems[adapterPosition].imdbID)
        }
    }

    companion object {
        private val UNAVAILABLE_IMAGE = "N/A"
    }
}