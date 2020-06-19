package com.vp.detail.mapper

import com.vp.data.mapper.Mapper
import com.vp.data.model.MovieFavorite
import com.vp.detail.model.MovieDetail

class FavoriteToDetailMapper : Mapper<MovieFavorite, MovieDetail> {

    override fun map(i: MovieFavorite): MovieDetail {
        return MovieDetail(
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
