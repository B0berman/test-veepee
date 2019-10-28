package com.vp.favorites.utils

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

class SchedulerProvider(private val backgroundScheduler: Scheduler, private val foregroundScheduler: Scheduler) {

    fun <T> forSingle(): (Single<T>) -> Single<T> {
        return { single: Single<T> ->
            single.subscribeOn(backgroundScheduler)
                .observeOn(foregroundScheduler)
        }
    }

    fun forCompletable(): (Completable) -> Completable {
        return { completable: Completable ->
            completable.subscribeOn(backgroundScheduler)
                .observeOn(foregroundScheduler)
        }
    }
}