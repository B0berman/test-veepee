package com.vp.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.vp.favorites.core.FavoriteInteractor
import com.vp.favorites.core.model.Events
import com.vp.favorites.core.model.State
import com.vp.favorites.core.model.room.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(favoriteInteractor: FavoriteInteractor) : ViewModel() {


    private val eventProcessor : BehaviorProcessor<Events> = BehaviorProcessor.create()
    private val compositeDisposable : CompositeDisposable = CompositeDisposable()

    val state : LiveData<State>

    val movieList : LiveData<List<Movie>>

    init {
        val refreshEvent = eventProcessor.ofType(Events.refreshFavoriteMovieList::class.java)
                .flatMapSingle { favoriteInteractor.getAllMovie() }

        val stateEvent = refreshEvent.map {
            if(it.isEmpty())
                return@map State.Error
            else return@map State.Success
        }

        compositeDisposable.add(stateEvent.subscribe())

        movieList = LiveDataReactiveStreams.fromPublisher(refreshEvent)

        state = LiveDataReactiveStreams.fromPublisher(stateEvent)
    }

    fun send(events : Events) = eventProcessor.onNext(events)

}