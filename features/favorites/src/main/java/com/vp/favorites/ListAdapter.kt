package com.vp.favorites

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.favorites.extensions.inflate
import com.vp.favorites.model.ListItem
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder?>() {

    private val listItems: MutableList<ListItem> = mutableListOf()

    companion object {
        private const val NO_IMAGE = "N/A"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(parent.inflate(R.layout.item_list))

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listItems[position])
    }

    override fun getItemCount(): Int = listItems.size

    fun setItems(data: List<ListItem>) {
        listItems.clear()
        listItems.addAll(data)
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal fun bind(item: ListItem) {
            itemView.title?.text = item.title
            Glide
                .with(itemView.poster)
                .load(item.poster)
                .into(itemView.poster)
        }
    }
}
