package androidx.lifecycle

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun <T> LiveData<T>.awaitNonNull(): T = suspendCancellableCoroutine { cont ->
    val observer = object : Observer<T?> {
        override fun onChanged(t: T?) {
            if (t != null) {
                removeObserver(this)
                cont.resume(t)
            }
        }
    }
    cont.invokeOnCancellation { removeObserver(observer) }
    observeForever(observer)
}