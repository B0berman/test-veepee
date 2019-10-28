package com.vp.favorites.interactor

import com.vp.favorites.repository.FavoriteRepository
import com.vp.favorites.utils.SchedulerProvider
import javax.inject.Inject

class DeleteMovieAsFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository,
    private val schedulerProvider: SchedulerProvider
): UseCase<String, Nothing>() {

    override var callback: Callback<Nothing>? = null

    override fun execute(params: String) {
        disposable = repository.deleteMovie(params)
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