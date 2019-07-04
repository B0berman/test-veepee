package com.vp.list.viewmodel

import com.vp.list.service.model.SearchResponse
import com.vp.list.viewmodel.model.ListItem

class ListItemModelMapper {

    fun toListItem(item: SearchResponse.Item): ListItem = ListItem(
            title = item.title ?: "",
            year = item.year ?: "",
            imdbID = item.imdbID ?: "",
            poster = item.poster
    )
}