package com.nullpointer.newscompose.core.utils

import com.nullpointer.newscompose.R
import timber.log.Timber

object ExceptionManager {
    const val NO_NETWORK_ERROR="NO_NETWORK_ERROR"
    const val NO_MORE_REQUESTS_ERROR="rateLimited"
    const val ERROR_INTERNAL_SERVER="unexpectedError"


    fun getMessageForException(throwable: Throwable,message: String):Int{
        return getMessageForException(Exception(throwable),message)
    }

    fun getMessageForException(exception: Exception,message: String): Int {
        Timber.e("${message}: $exception")
        return if (exception is NullPointerException) {
            R.string.server_response_null
        } else {
            when (exception.message) {
                NO_NETWORK_ERROR -> R.string.error_network
                NO_MORE_REQUESTS_ERROR->R.string.error_no_more_requets
                ERROR_INTERNAL_SERVER -> R.string.error_internal_server
                else -> R.string.error_unknown
            }
        }
    }

}