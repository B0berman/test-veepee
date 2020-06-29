package com.vp.favorites

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vp.detail.model.MovieDetail

internal class DetailAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val data = mutableListOf<MovieDetail>()
    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        val item = data[position]
        return DetailFragment.newInstance(item)
    }

    fun setItems(items: List<MovieDetail>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }
}