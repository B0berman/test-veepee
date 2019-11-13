package com.vp.movies.data.remote.retrofit.adapter

import com.vp.movies.data.remote.retrofit.exception.RetrofitException
import io.reactivex.*
import io.reactivex.functions.Function
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory private constructor() : CallAdapter.Factory() {

    private val original: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        return RxCallAdapterWrapper(
            retrofit,
            original.get(returnType, annotations, retrofit)
        )
    }

    private class RxCallAdapterWrapper<R>(private val retrofit: Retrofit, private val wrapped: CallAdapter<R, *>?) : CallAdapter<R, Any> {

        override fun adapt(call: Call<R>): Any? {
            return when (val result = wrapped?.adapt(call)) {
                is Single<*> -> result.onErrorResumeNext(Function { throwable -> Single.error(asRetrofitException(throwable)) })
                is Maybe<*> -> result.onErrorResumeNext(Function { throwable -> Maybe.error(asRetrofitException(throwable)) })
                is Completable -> result.onErrorResumeNext { throwable -> Completable.error(asRetrofitException(throwable)) }
                is Flowable<*> -> result.onErrorResumeNext(Function { throwable -> Flowable.error(asRetrofitException(throwable)) })
                is Observable<*> -> result.onErrorResumeNext(Function { throwable -> Observable.error(asRetrofitException(throwable)) })
                else -> result
            }
        }

        private fun asRetrofitException(throwable: Throwable): RetrofitException {
            // We had non-200 http error
            if (throwable is HttpException) {
                val url  = throwable.response()?.raw()?.request()?.url().toString()
                throwable.response()?.let {
                    return when {
                        it.code() == 401 -> RetrofitException.unauthenticatedError(url, it, retrofit)
                        it.code() in 400..499 -> RetrofitException.clientError(url, it, retrofit)
                        it.code() in 500..599 -> RetrofitException.serverError(url, it, retrofit)
                        else -> RetrofitException.unexpectedError(throwable)
                    }
                }
            }
            // A network error happened
            return if (throwable is IOException) RetrofitException.networkError(throwable)
            else RetrofitException.unexpectedError(throwable)
            // We don't know what happened. We need to simply convert to an unknown error
        }

        override fun responseType(): Type? {
            return wrapped?.responseType()
        }
    }

    companion object {

        fun create(): CallAdapter.Factory {
            return RxErrorHandlingCallAdapterFactory()
        }
    }
}