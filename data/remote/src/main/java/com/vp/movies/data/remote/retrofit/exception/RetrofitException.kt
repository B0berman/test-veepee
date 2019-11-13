package com.vp.movies.data.remote.retrofit.exception

import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

/**
 * Created by Raxden on 25/12/2016.
 */

class RetrofitException internal constructor(
    /** The request URL which produced the error.  */
    val url: String?,
    /** Response object containing status code, headers, body, etc.  */
    val response: Response<*>?,
    /** The event kind which triggered this error.  */
    val kind: Kind, exception: Throwable?,
    /** The Retrofit this request was executed on  */
    val retrofit: Retrofit?
) : RuntimeException(exception) {

    var parsedBody: Any? = null

    /** Identifies the event kind which triggered a [RetrofitException].  */
    enum class Kind {
        /** An [IOException] occurred while communicating to the server.  */
        NETWORK,
        /** A non-200 HTTP status code was received from the server.  */
        UNAUTHENTICATED,
        CLIENT_ERROR,
        SERVER_ERROR,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun <T> parseBodyErrorAs(type: Class<T>): T? {
        if (parsedBody != null) return parsedBody as T
        if (response?.errorBody() == null) {
            return null
        }
        val converter = retrofit?.responseBodyConverter<T>(type, arrayOfNulls(0))
        parsedBody = converter?.convert(response.errorBody()!!)
        return parsedBody as T
    }

    companion object {

        fun unauthenticatedError(
            url: String,
            response: Response<*>,
            retrofit: Retrofit
        ): RetrofitException {
            return RetrofitException(url, response, Kind.UNAUTHENTICATED, null, retrofit)
        }

        fun clientError(url: String, response: Response<*>, retrofit: Retrofit): RetrofitException {
            return RetrofitException(url, response, Kind.CLIENT_ERROR, null, retrofit)
        }

        fun serverError(url: String, response: Response<*>, retrofit: Retrofit): RetrofitException {
            return RetrofitException(url, response, Kind.SERVER_ERROR, null, retrofit)
        }

        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(null, null, Kind.NETWORK, exception, null)
        }

        fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(null, null, Kind.UNEXPECTED, exception, null)
        }
    }

}
