package com.vp.list

import androidx.lifecycle.Observer

class TestObserver<T> : Observer<T> {

    private var observedValues: T? = null

    override fun onChanged(t: T) {
        observedValues = t
    }
}