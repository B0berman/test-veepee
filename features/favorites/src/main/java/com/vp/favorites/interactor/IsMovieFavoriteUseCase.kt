package com.vp.favorites.interactor

import com.vp.favorites.repository.FavoriteRepository
import com.vp.favorites.utils.SchedulerProvider
import javax.inject.Inject

class IsMovieFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository,
    private val schedulerProvider: SchedulerProvider
): UseCase<String, Boolean>() {

    override var callback: Callback<Boolean>? = null

    override fun execute(params: String) {
        disposable = repository.isMovieFav(params)
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