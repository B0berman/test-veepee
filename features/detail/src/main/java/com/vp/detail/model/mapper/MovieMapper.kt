package com.vp.detail.model.mapper

import com.vp.detail.model.MovieDetail
import com.vp.favorites.model.BasicMovie

object MovieMapper {
    fun transformFromDetail(id: String, detail: MovieDetail) : BasicMovie {
        return BasicMovie(id, detail.title, detail.poster)
    }
}