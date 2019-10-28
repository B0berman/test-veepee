package com.vp.favorites.interactor

import com.vp.favorites.model.BasicMovie
import com.vp.favorites.repository.FavoriteRepository
import com.vp.favorites.utils.SchedulerProvider
import javax.inject.Inject

class StoreMovieAsFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository,
    private val schedulerProvider: SchedulerProvider
): UseCase<BasicMovie, Nothing>() {

    override var callback: Callback<Nothing>? = null

    override fun execute(params: BasicMovie) {
        disposable = repository.addMovie(params)
            .compose(schedulerProvider.forCompletable())
            .subscribe(
                {
                    callback?.onComplete()
                },
                {
                    callback?.onError(it)
                }
            )
    }
}