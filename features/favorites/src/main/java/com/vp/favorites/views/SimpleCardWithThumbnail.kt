package com.vp.favorites.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.vp.favorites.R
import kotlinx.android.synthetic.main.view_simple_card_thumbnail.view.*


class SimpleCardWithThumbnail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr){

    init {
        LayoutInflater.from(context).inflate(R.layout.view_simple_card_thumbnail, this)
    }

    fun setImage(path: String) {
        cardThumbnail?.setThumbnail(path)
    }

    fun setTitle(title: String) {
        cardTitle?.text = title
    }

    fun setLogo(logo: String) {
        cardThumbnail?.setLogo(logo)
    }

    fun setAction(listener: () -> Unit) {
        this.setOnClickListener{
            listener.invoke()
        }
    }
}
