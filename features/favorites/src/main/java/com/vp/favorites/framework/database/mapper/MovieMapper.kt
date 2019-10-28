package com.vp.favorites.framework.database.mapper

import com.vp.favorites.framework.database.model.MovieEntity
import com.vp.favorites.model.BasicMovie

object MovieMapper {

    fun transformToEntity(movie: BasicMovie) : MovieEntity {
        return with(movie) {
            MovieEntity(id, title, poster)
        }
    }

    fun transformFromEntity(entity: MovieEntity) : BasicMovie {
        return with(entity) {
            BasicMovie(id, title, poster)
        }
    }

    fun transformFromEntity(entities: List<MovieEntity>) : List<BasicMovie> {
        return entities.map { transformFromEntity(it) }
    }
}