package com.vp.persistence.extension

fun <K, V> Map<out K, V>.getOrNullableCompat(key: K): V? {
    if (this.containsKey(key)) {
        return this[key]
    }
    return null
}