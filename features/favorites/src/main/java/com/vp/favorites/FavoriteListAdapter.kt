package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vp.database.beans.Movie
import com.vp.list.GlideApp
import com.vp.list.R

class FavoriteListAdapter : RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {
    private var items = emptyList<Movie>()
    private var onItemClickListener : OnItemClickListener? = null
    private val NO_IMAGE = "N/A"

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    fun setItems(newItems: List<Movie>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FavoriteListAdapter.ViewHolder, position: Int) {
        val item: Movie = items[position]
        if (NO_IMAGE != item.poster) {
            val density = holder.image.resources.displayMetrics.density
            GlideApp.with(holder.image)
                    .load(item.poster)
                    .override((300 * density).toInt(), (600 * density).toInt())
                    .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View) {
            onItemClickListener?.onItemClick(items[adapterPosition].id)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String?)
    }
}