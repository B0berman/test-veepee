package saha.tushar.common.db

import androidx.lifecycle.LiveData
import com.tushar.todosample.db.FavMovieDb
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class FavMovieRepository @Inject constructor(fabMovieDb: FavMovieDb) {
    private val favMovieDao: FavMovieDao = fabMovieDb.favMovieDao()
    private val dbWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

    fun addFavourite(favMovie: FavMovie, onSuccess: (Long) -> Unit) {
        dbWriteExecutor.execute {
            onSuccess.invoke(favMovieDao.addFavourite(favMovie))
        }
    }

    fun getFavourites(): LiveData<List<FavMovie>> = favMovieDao.getAllFavourites()

    companion object {
        private const val NUMBER_OF_THREADS = 4
    }
}