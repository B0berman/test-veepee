package com.vp.list.model

abstract class ListItem {

    companion object {
        const val DATA_ITEM = 1
        const val PROGRESS_ITEM = 0
    }

    abstract val type: Int
}