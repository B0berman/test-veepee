package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

import com.vp.daggeraddons.GlideApp
import com.vp.favorites.model.FavouriteItem

import java.util.Collections

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }

    private var listItems: MutableList<FavouriteItem> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null

    companion object {
        private val NO_IMAGE = "N/A"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = listItems[position]

        if (NO_IMAGE != currentItem.poster) {
            val density = holder.image.resources.displayMetrics.density
            GlideApp
                    .with(holder.image)
                    .load(currentItem.poster)
                    .override((300 * density).toInt(), (600 * density).toInt())
                    .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(listItems: List<FavouriteItem>) {
        this.listItems = listItems.toMutableList()
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View) {
            onItemClickListener?.onItemClick(listItems[adapterPosition].imdbID)
        }
    }

}
