package com.vp.favorites.interactor

import com.vp.favorites.model.BasicMovie
import com.vp.favorites.repository.FavoriteRepository
import com.vp.favorites.utils.SchedulerProvider
import javax.inject.Inject

class GetMyFavoriteMoviesUseCase @Inject constructor(
    private val repository: FavoriteRepository,
    private val schedulerProvider: SchedulerProvider
): UseCase<UseCase.EmptyParams, List<BasicMovie>>() {

    override var callback: Callback<List<BasicMovie>>? = null

    override fun execute(params: EmptyParams) {
        disposable = repository.getAllMovies()
            .compose(schedulerProvider.forSingle())
            .subscribe(
                {
                    callback?.onSuccess(it)
                },
                {
                    callback?.onError(it)
                }
            )
    }
}