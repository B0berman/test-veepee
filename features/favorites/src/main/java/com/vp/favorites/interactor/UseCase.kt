package com.vp.favorites.interactor

import io.reactivex.disposables.Disposable

abstract class UseCase<P, R> {

    abstract var callback: Callback<R>?

    protected var disposable: Disposable? = null

    abstract fun execute(params: P)

    fun cancel() {
        disposable?.dispose()
    }

    object EmptyParams

    interface Callback<R> {
        fun onSuccess(result: R)
        fun onComplete()
        @Throws(Exception::class)
        fun onError(result: Throwable)
    }
}