package com.vp.detail.viewmodel

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams.fromPublisher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vp.detail.model.Events
import com.vp.detail.model.FavoriteInfo
import com.vp.detail.model.MovieDetail
import com.vp.detail.model.MovieResponse
import com.vp.detail.service.DetailService
import com.vp.favorites.core.FavoriteInteractor
import com.vp.favorites.core.model.room.Movie
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

/* Short demo for having an event flow, in a real App the base would be this and more elaborated(Generics etc..) - CE */
class DetailsViewModel @Inject constructor(private val detailService: DetailService, favoriteInteractor : FavoriteInteractor) : ViewModel() {

    val details: LiveData<MovieDetail>
    val loadingState: LiveData<LoadingState>

    private val theEventProcessor : BehaviorProcessor<Events> = BehaviorProcessor.create()

    private val compositeDisposable : CompositeDisposable = CompositeDisposable()

    val isMovieInFavorite : MutableLiveData<FavoriteInfo> = MutableLiveData()
    private val favoriteInfo : FavoriteInfo = FavoriteInfo()

    init {

        val events = theEventProcessor

        val fetchDetailsFlow = events.ofType(Events.GetMovieDetails::class.java).map {
            return@map it.movieId
        }.doOnNext {
            println("Rx : Movie details request received $it")
        }.compose(fetchDetails())
        .doOnNext {
            println("Rx : Movie response received $it")
               }.share()

        val content = fetchDetailsFlow.ofType(MovieResponse.Success::class.java).map {
            return@map it.movieDetail
        }

        val refreshFavoriteState = content.flatMapSingle {
            return@flatMapSingle favoriteInteractor.isMovieInFavorite(it.id) }
                .doOnNext { isMovieInFavorite.value = favoriteInfo.also { favoriteInfo ->
                    favoriteInfo.isInFavorite = it
                    favoriteInfo.isNewlyAdded = false
                }}

        val addOrRemoveFavoriteFlow = events.ofType(Events.AddOrRemoveFavorite::class.java)
                .map {
                    if(it.remove)
                        return@map it.movie.id

                   return@map Movie(it.movie.id, it.movie.title, it.movie.year, it.movie.runtime, it.movie.director, it.movie.plot, it.movie.poster)
                }
                .flatMapCompletable {
                    when(it) {
                        is Movie -> {
                            isMovieInFavorite.value = favoriteInfo.also { favoriteInfo ->
                                favoriteInfo.isInFavorite = true
                                favoriteInfo.isNewlyAdded = true
                            }
                            return@flatMapCompletable favoriteInteractor.addMovieToFavorite(it)
                        }
                        else -> {
                            if(it is String) {
                                isMovieInFavorite.value = favoriteInfo.also { favoriteInfo ->
                                    favoriteInfo.isInFavorite = false
                                    favoriteInfo.isNewlyAdded = false
                                }
                                return@flatMapCompletable favoriteInteractor.removeFromFavorite(it)
                            }

                            return@flatMapCompletable Completable.complete()
                        }
                    }
                }

        // Example if We want to have a State - CE
        val state = fetchDetailsFlow.map {
            when(it) {
                is MovieResponse.Error -> LoadingState.ERROR
                is MovieResponse.Loading -> LoadingState.IN_PROGRESS
                is MovieResponse.Success -> LoadingState.LOADED
            }
        }

        compositeDisposable.addAll(state.subscribe(), addOrRemoveFavoriteFlow.subscribe(), refreshFavoriteState.subscribe())

        details = fromPublisher(content)

        loadingState = fromPublisher(state)

    }


    fun title(): LiveData<String> = Transformations.map(details, Function {
        return@Function it.title
    })

    fun sendEvent(event : Events) = theEventProcessor.onNext(event)

    private fun  fetchDetails() : FlowableTransformer<String, MovieResponse> {

        return FlowableTransformer {

            return@FlowableTransformer it.flatMap { movieId ->

               return@flatMap Observable.create<MovieResponse> { singleEmmiter ->
                   singleEmmiter.onNext(MovieResponse.Loading)
                    detailService.getMovie(movieId).enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
                        override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                            if(response!!.isSuccessful) {
                                singleEmmiter.onNext(MovieResponse.Success(response.body()!!))
                                singleEmmiter.onComplete()
                            }
                            else {
                                singleEmmiter.onError(Throwable("Error"))
                            }
                        }

                        override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                            singleEmmiter.onError(Throwable("Error"))
                        }
                    })
                }.onErrorReturn { MovieResponse.Error(it.localizedMessage) }.toFlowable(BackpressureStrategy.LATEST)
            }

        }

        /* 'DetailActivity.queryProvider.getMovieId()' */

    }

    fun state() = loadingState

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    // ToDo : replace by a UI State Response with Sealed Class. - CE
    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}