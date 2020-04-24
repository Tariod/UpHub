package com.tariod.uphub.data.api.github

import com.tariod.uphub.data.api.ApiException
import com.tariod.uphub.data.api.ApiException.Companion.AUTH_LIMIT
import com.tariod.uphub.data.api.ApiException.Companion.NO_CONNECTION
import com.tariod.uphub.data.api.ApiException.Companion.UNKNOWN_CODE
import com.tariod.uphub.data.api.ApiException.Companion.UN_AUTH
import com.tariod.uphub.data.api.ErrorHandler
import retrofit2.HttpException
import java.net.UnknownHostException

class GithubErrorHandler : ErrorHandler {

    private var lastRequestLimit = -1L

    override fun handleError(exception: Throwable): ApiException = when (exception) {
        is HttpException ->
            ApiException(when (exception.code()) {
                403 -> AUTH_LIMIT.also {
                    lastRequestLimit = System.currentTimeMillis()
                }
                401 -> UN_AUTH
                else -> UNKNOWN_CODE
            }, exception)

        is UnknownHostException ->
            ApiException(NO_CONNECTION, exception)

        else ->
            ApiException(UNKNOWN_CODE, exception)
    }
}