package com.tariod.uphub.data.api

interface ErrorHandler {

    fun handleError(exception: Throwable): ApiException
}