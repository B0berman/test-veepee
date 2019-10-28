package com.vp.favorites.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.vp.favorites.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.view_thumbnail_w_gradient.view.*

class ThumbnailWithGradient @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_thumbnail_w_gradient, this)
    }

    fun setThumbnail(path: String) {
        val multiTransformation = MultiTransformation(
            CenterCrop(),
            RoundedCornersTransformation(
                ROUNDED_CORNER,
                ROUNDED_CORNER_MARGIN,
                RoundedCornersTransformation.CornerType.TOP
            )
        )

        Glide.with(context)
            .load(path)
            .apply(RequestOptions.bitmapTransform(multiTransformation))
            .into(thumbnail)
    }

    fun setLogo(data: String) {
        logo?.text = data
    }

    companion object {
        private const val ROUNDED_CORNER = 20
        private const val ROUNDED_CORNER_MARGIN = 2
    }
}
