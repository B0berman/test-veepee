package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

import com.vp.detail.model.MovieDetail
import com.vp.list.GlideApp

import java.util.Collections

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.ListViewHolder>() {
    private var listItems = emptyList<MovieDetail>()
    private val EMPTY_ON_ITEM_CLICK_LISTENER: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(imdbID: String) {
            //empty listener
        }
    }
    private var onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listItem = listItems[position]

        if (NO_IMAGE != listItem.poster) {
            val density = holder.image.resources.displayMetrics.density
            GlideApp
                    .with(holder.image)
                    .load(listItem.poster)
                    .override((300 * density).toInt(), (600 * density).toInt())
                    .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    internal fun setItems(listItems: List<MovieDetail>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    internal fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
        }
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View) {
            onItemClickListener.onItemClick(listItems[adapterPosition].imdbID)
        }
    }

    internal interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }

    companion object {
        private val NO_IMAGE = "N/A"
    }
}
