package com.vp.favorites

import androidx.recyclerview.widget.DiffUtil
import com.vp.favorites.viewmodel.model.FavoriteMovie

internal class FavoriteDiffCallback(
        private val oldList: List<FavoriteMovie>,
        private val newList: List<FavoriteMovie>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size
}