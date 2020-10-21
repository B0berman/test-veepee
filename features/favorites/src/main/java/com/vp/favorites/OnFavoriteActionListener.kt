package com.vp.favorites

interface OnFavoriteActionListener {
    fun onItemClicked(id: String)
    fun onRemove(id: String)
}
