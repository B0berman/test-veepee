package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.gmail.saneme87.glidemodule.GlideApp
import com.vp.list.model.ListItem
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.loading_list.view.*

class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_ITEM = 1
        private const val NO_IMAGE = "N/A"
        private val EMPTY_ON_ITEM_CLICK_LISTENER: OnItemClickListener = object : OnItemClickListener {
            override fun onItemClick(imdbID: String?) {
            }
        }
    }

    private var listItems = mutableListOf<ListItem>()
    private var totalResult = 0
    private var onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            ListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
        } else {
            ListLoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.loading_list, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingItemPos(position)) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    private fun isLoadingItemPos(position: Int): Boolean {
        return (position >= listItems.size && listItems.size <= totalResult)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ListItemViewHolder) {
            val (_, _, _, poster) = listItems[position]
            if (poster != null && NO_IMAGE != poster) {
                val density = holder.image.resources.displayMetrics.density
                GlideApp
                        .with(holder.image)
                        .load(poster)
                        .override((300 * density).toInt(), (600 * density).toInt())
                        .into(holder.image)
            } else {
                holder.image.setImageResource(R.drawable.placeholder)
            }
        } else if (holder is ListLoadingViewHolder) {
            holder.progressBar.visibility = if (listItems.size < totalResult) VISIBLE else GONE
        }
    }

    override fun getItemCount(): Int {
        return listItems.size + 1
    }

    fun setItems(listItems: List<ListItem>) {
        this.listItems = listItems.toMutableList()
        notifyDataSetChanged()
    }

    fun setTotalResult(totalResult: Int) {
        this.totalResult = totalResult
    }

    fun clearItems() {
        listItems.clear()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
        }
    }

    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView

        override fun onClick(v: View) {
            onItemClickListener.onItemClick(listItems[adapterPosition].imdbID)
        }

        init {
            itemView.setOnClickListener(this)
            image = itemView.poster
        }

    }

    inner class ListLoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar = itemView.progressBar
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String?)

    }
}