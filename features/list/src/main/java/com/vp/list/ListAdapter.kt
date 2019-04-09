package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vp.list.model.ListItem


class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listItems: MutableList<ListItem> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null

    private var isLoadingAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM -> {
                val viewItem = inflater.inflate(R.layout.item_list, parent, false)
                ListViewHolder(viewItem)
            }
            LOADING -> {
                val viewLoading = inflater.inflate(R.layout.item_loading, parent, false)
                LoadingViewHolder(viewLoading)
            }

            else -> {
                val viewItem = inflater.inflate(R.layout.item_list, parent, false)
                ListViewHolder(viewItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                val (_, _, _, poster) = listItems[position]
                holder as ListViewHolder
                if (poster != null && NO_IMAGE != poster) {
                    val density = holder.image.resources.displayMetrics.density
                    GlideApp
                            .with(holder.image)
                            .load(poster)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .override((300 * density).toInt(), (600 * density).toInt())
                            .error(R.drawable.placeholder)
                            .into(holder.image)
                } else {
                    holder.image.setImageResource(R.drawable.placeholder)
                }
            }
            LOADING -> {
                listItems[position]
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == listItems.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun setItems(listItems: List<ListItem>) {
        this.listItems = listItems.toMutableList()
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems = mutableListOf()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        listItems.add(ListItem(null, null, null, null))
        notifyDataSetChanged()
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        if (!listItems.isEmpty()) {
            val position = listItems.size - 1
            val result: ListItem? = listItems[position]
            if (result != null) {
                listItems.removeAt(position)
                notifyItemRemoved(position)
            }
        }
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

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val NO_IMAGE = "N/A"

        const val LOADING = 0
        const val ITEM = 1
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String?)
    }
}