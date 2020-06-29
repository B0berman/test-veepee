package com.vp

import retrofit2.Call
import java.util.*

class CompositeDisposableCalls {
    private val calls = mutableListOf<Call<*>>()
    fun add(call: Call<*>) {
        calls.add(call)
    }

    fun cancel() {
        for (i in calls.indices) {
            val call = calls[i]
            call.cancel()
        }
    }
}