package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.model.ListItem
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter(
    private val listItems: MutableList<ListItem> = mutableListOf()
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private val emptylistener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(imdbID: String) {
            //empty listener
        }
    }

    private var onItemClickListener =
        emptylistener

    companion object {
        private const val NO_IMAGE: String = "N/A"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        val listItem = listItems[position]
        if (listItem.poster != null && NO_IMAGE != listItem.poster) {
            val density = holder.image?.resources?.displayMetrics?.density ?: 1f
            holder.image?.let {
                val width: Int = (300 * density).toInt()
                val height: Int = (600 * density).toInt()
                GlideApp
                    .with(it)
                    .load(listItem.poster)
                    .override(width, height)
                    .into(it)
            }
        } else {
            holder.image?.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(list: List<ListItem>) {
        listItems.clear()
        listItems.addAll(list)
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        } else {
            this.onItemClickListener = emptylistener
        }
    }

    inner class ListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView?
        override fun onClick(v: View) {
            listItems[adapterPosition].imdbID?.let {
                onItemClickListener.onItemClick(it)
            }
        }

        init {
            itemView.setOnClickListener(this)
            image = itemView.poster
        }
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }
}

