package com.vp.favorites.utils

import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


fun <T> Single<T>.composeWithBackgroundThreadSchedulers(): Single<T> {

    return compose(SingleTransformer {

        return@SingleTransformer it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    })
}

 fun Completable.composeWithBackgroundThreadSchedulers() : Completable {

    return compose(CompletableTransformer {

        return@CompletableTransformer it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    })
}