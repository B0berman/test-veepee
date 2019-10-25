package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.model.ListItem
import com.vp.list.model.Movie
import com.vp.list.model.MovieItem
import com.vp.list.model.ProgressItem
import kotlinx.android.synthetic.main.item_list.view.*


class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var items: MutableList<ListItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    internal var onItemClickListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ListItem.PROGRESS_ITEM -> ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false))
            ListItem.DATA_ITEM -> ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
            else -> throw UnsupportedOperationException("Not valid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ListViewHolder -> {
                val movieItem = items[position] as MovieItem
                holder.bind(movieItem.movie)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun clearItems() {
        items.clear()
    }

    fun showLoadingIndicator() {
        items.add(items.lastIndex + 1, ProgressItem())
        notifyItemInserted(items.lastIndex + 1)
    }

    fun hideLoadingIndicator() {
        if (items.isNotEmpty() && items.last() is ProgressItem){
            items.remove(items.last())
            notifyItemRemoved(items.lastIndex + 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie) {
            if (item.poster != null && NO_IMAGE != item.poster) {
                val density = itemView.poster.resources.displayMetrics.density
                GlideApp
                        .with(itemView.poster)
                        .load(item.poster)
                        .override((300 * density).toInt(), (600 * density).toInt())
                        .into(itemView.poster)
            } else {
                itemView.poster.setImageResource(R.drawable.placeholder)
            }

            itemView.setOnClickListener { onItemClickListener.invoke(item.imdbID) }
        }
    }

    inner class ProgressViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    companion object {
        private val NO_IMAGE = "N/A"
    }
}
