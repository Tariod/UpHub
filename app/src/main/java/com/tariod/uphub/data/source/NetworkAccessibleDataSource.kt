package com.tariod.uphub.data.source

import androidx.paging.PageKeyedDataSource
import com.tariod.uphub.data.api.ErrorHandler
import com.tariod.uphub.utilities.coroutine.JobBuilder
import kotlinx.coroutines.CoroutineScope

abstract class NetworkAccessibleDataSource<K, V>(private val errorHandler: ErrorHandler) :
    PageKeyedDataSource<K, V>() {

    fun <T> doJob(action: suspend CoroutineScope.() -> T) = JobBuilder(action, errorHandler)
}