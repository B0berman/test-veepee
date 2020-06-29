package com.vp.list.search

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.annotation.Keep
import androidx.appcompat.R
import androidx.appcompat.widget.SearchView

interface OnSubmitQueryListener {
    fun onSubmitSearchQuery(query: String)
}

// NOTE: Android won't save the state of this widget properly, so the Activity/Fragment that has a reference to this class needs to
// save and restore the state for us.
@Keep
class CustomSearchView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.searchViewStyle
) : SearchView(context, attrs, defStyleAttr) {

    private var listener: OnSubmitQueryListener? = null

    init {

        imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        isIconified = true

        setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                listener?.onSubmitSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    fun setOnSubmitQueryListener(listener: OnSubmitQueryListener) {
        this.listener = listener
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("super", super.onSaveInstanceState())
        bundle.putString("query", query?.toString())
        bundle.putBoolean("isIconified", isIconified)
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val bundle = state as Bundle
        super.onRestoreInstanceState(state.getParcelable("super"))
        setQuery(bundle.getString("query"), false)
        isIconified = bundle.getBoolean("isIconified")
    }
}