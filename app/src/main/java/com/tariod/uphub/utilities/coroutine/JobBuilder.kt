package com.tariod.uphub.utilities.coroutine

import com.tariod.uphub.data.api.ApiException
import com.tariod.uphub.data.api.ErrorHandler
import kotlinx.coroutines.*

class JobBuilder<T>(val action: suspend CoroutineScope.() -> T, val errorHandler: ErrorHandler) {

    private var job = Job()

    private val apiHandlers = mutableListOf<(ApiException) -> Unit>()

    @Suppress("ThrowableNotThrown")
    private var coroutineErrorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        errorHandler.handleError(e).let { ex ->
            synchronized(apiHandlers) {
                apiHandlers.forEach { it(ex) }
            }
        }
    }

    fun handleError(handler: (ApiException) -> Unit): JobBuilder<T> {
        synchronized(apiHandlers) {
            apiHandlers.add(handler)
        }
        return this
    }

    fun run(jobComp: JobComposite? = null) {
        GlobalScope.launch(Dispatchers.IO + coroutineErrorHandler + job) {
            action()
        }
        keepIn(jobComp ?: return)
    }

    fun keepIn(jobComp: JobComposite): JobBuilder<T> {
        jobComp.add(job)
        return this
    }
}