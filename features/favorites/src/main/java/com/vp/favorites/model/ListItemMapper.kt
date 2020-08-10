package com.vp.favorites.model

import com.vp.list.model.ListItem
import com.vp.storage.model.MovieItemDTO
import javax.inject.Inject

class ListItemMapper @Inject constructor(){

    fun fromMovieItemDTO(movieItemDTO : MovieItemDTO) = ListItem(
            movieItemDTO.title,
            movieItemDTO.year,
            movieItemDTO.imdbID,
            movieItemDTO.poster
    )
}