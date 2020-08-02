package com.vp.detail

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide


private const val NO_IMAGE = "N/A"

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String?) {
    url?.let{
        if(url != NO_IMAGE) {
            Glide
                    .with(view)
                    .load(url)
                    .into(view)
        } else view.setImageResource(R.drawable.placeholder)
    } ?: view.setImageResource(R.drawable.placeholder)
}
