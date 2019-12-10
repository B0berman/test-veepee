package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail.saneme87.glidemodule.GlideApp
import com.vp.favorites.model.ListItem
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter(private val itemList: List<ListItem>,
                  private val onClickListener: (String) -> Unit)
    : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClickListener(item.id)
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ListItem) {
            if ("N/A" != item.poster) {
                val density: Float = itemView.poster.resources.displayMetrics.density
                GlideApp
                        .with(itemView.poster)
                        .load(item.poster)
                        .override((300 * density).toInt(), (600 * density).toInt())
                        .into(itemView.poster)
            } else {
                itemView.poster.setImageResource(R.drawable.placeholder)
            }
        }
    }

}