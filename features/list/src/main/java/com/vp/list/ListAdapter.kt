package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.model.ListItem
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    internal var listItems: List<ListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    internal var onItemClickListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listItems[position])
    }

    override fun getItemCount(): Int = listItems.size

    fun clearItems() {
        listItems = emptyList()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: ListItem) {
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
        }

        override fun onClick(v: View) {
            onItemClickListener.invoke(listItems[adapterPosition].imdbID)
        }
    }

    companion object {
        private val NO_IMAGE = "N/A"
    }
}
