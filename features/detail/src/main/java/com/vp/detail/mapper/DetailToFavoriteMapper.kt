package com.vp.detail.mapper

import com.vp.data.mapper.Mapper
import com.vp.data.model.MovieFavorite
import com.vp.detail.model.MovieDetail

class DetailToFavoriteMapper : Mapper<MovieDetail, MovieFavorite> {

    override fun map(i: MovieDetail): MovieFavorite {
        return MovieFavorite(
                imdbID = i.imdbID,
                title = i.title,
                year = i.year,
                runtime = i.runtime,
                director = i.director,
                plot = i.plot,
                poster = i.poster
        )
    }
}
