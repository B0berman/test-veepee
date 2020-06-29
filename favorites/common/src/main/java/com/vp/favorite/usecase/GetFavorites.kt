package com.vp.favorite.usecase

import com.vp.CompositeDisposableCalls
import com.vp.detail.model.MovieDetail
import com.vp.detail.service.DetailService
import com.vp.favorite.service.FavoritesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

typealias OnSuccess = (List<MovieDetail>) -> Unit
typealias  OnError = (error: Throwable) -> Unit

class GetFavorites @Inject constructor(
        private val detailService: DetailService,
        private val favoritesService: FavoritesService
) {

    fun execute(onSuccess: OnSuccess, onError: OnError): CompositeDisposableCalls {
        val disposableCalls = CompositeDisposableCalls()

        val calls = favoritesService.getAllFavorite()
                .map { id ->
                    detailService.getMovie(id)
                }
        if (calls.isEmpty()) {
            onSuccess(emptyList())
        } else {
            val callback = CompositeCallback(calls, onSuccess, onError)
            calls.forEach { call ->
                disposableCalls.add(call)
                call.enqueue(callback)
            }
        }
        return disposableCalls
    }
}

internal class CompositeCallback(
        private val calls: List<Call<*>>,
        private val onSuccess: OnSuccess,
        private val onError: OnError
) : Callback<MovieDetail> {
    private val results = mutableListOf<MovieDetail>()
    private val errors = mutableListOf<Throwable>()

    private fun deliverResult() {
        if (results.size + errors.size == calls.size) {
            if (errors.isEmpty()) {
                onSuccess(results.sortedBy { it.title })
            } else {
                onError(CompositeExecutionException(errors))
            }
        }
    }

    override fun onFailure(call: Call<MovieDetail>, error: Throwable) {
        synchronized(this) {
            errors.add(error)
            deliverResult()
        }
    }

    override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
        synchronized(this) {
            results.add(response.body()!!)
            deliverResult()
        }
    }
}