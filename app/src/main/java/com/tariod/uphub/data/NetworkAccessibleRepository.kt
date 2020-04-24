package com.tariod.uphub.data

import com.tariod.uphub.data.api.ErrorHandler
import com.tariod.uphub.utilities.coroutine.JobBuilder
import kotlinx.coroutines.CoroutineScope

abstract class NetworkAccessibleRepository(private val errorHandler: ErrorHandler) {

    fun <T> doJob(action: suspend CoroutineScope.() -> T) = JobBuilder(action, errorHandler)
}