package com.vp.storage.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.squareup.sqldelight.Query
import java.util.concurrent.Executor
import java.util.concurrent.Executors

fun <T : Any> Query<T>.asLiveData(): LiveData<Query<T>> =
        object : LiveData<Query<T>>() {

            private val listener = object : Query.Listener {
                override fun queryResultsChanged() {
                    postValue(this@asLiveData)
                }
            }

            override fun onActive() {
                addListener(listener)
                postValue(this@asLiveData)
            }

            override fun onInactive() {
                removeListener(listener)
            }
        }

private val ioExecutor: Executor by lazy {
    Executors.newSingleThreadExecutor()
}

fun <T : Any, U : Any> LiveData<Query<T>>.mapToList(
        executor: Executor = ioExecutor,
        mapper: (List<T>) -> List<U>
): LiveData<List<U>> {
    val result = MediatorLiveData<List<U>>()
    result.addSource(this) { query ->
        executor.execute { result.postValue(mapper(query.executeAsList())) }
    }
    return result
}

fun <T : Any, U : Any> LiveData<Query<T>>.mapToOne(
        executor: Executor = ioExecutor,
        mapper: (T) -> U
): LiveData<U> {
    val result = MediatorLiveData<U>()
    result.addSource(this) { query ->
        executor.execute { result.postValue(mapper(query.executeAsOne())) }
    }
    return result
}
